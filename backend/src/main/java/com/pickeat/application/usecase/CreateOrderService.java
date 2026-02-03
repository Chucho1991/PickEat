package com.pickeat.application.usecase;

import com.pickeat.domain.MenuItem;
import com.pickeat.domain.MenuItemId;
import com.pickeat.domain.Mesa;
import com.pickeat.domain.MesaId;
import com.pickeat.domain.Order;
import com.pickeat.domain.OrderChannel;
import com.pickeat.domain.OrderChannelId;
import com.pickeat.domain.OrderConfig;
import com.pickeat.domain.OrderDraft;
import com.pickeat.domain.OrderDiscountDraft;
import com.pickeat.domain.OrderDiscountItem;
import com.pickeat.domain.OrderItem;
import com.pickeat.domain.OrderItemDraft;
import com.pickeat.domain.TipType;
import com.pickeat.domain.Coupon;
import com.pickeat.domain.CouponStatus;
import com.pickeat.domain.DiscountItem;
import com.pickeat.domain.DiscountApplyScope;
import com.pickeat.domain.OrderId;
import com.pickeat.ports.in.CreateOrderUseCase;
import com.pickeat.ports.out.CouponRepositoryPort;
import com.pickeat.ports.out.DiscountItemRepositoryPort;
import com.pickeat.ports.out.MenuItemRepositoryPort;
import com.pickeat.ports.out.MesaRepositoryPort;
import com.pickeat.ports.out.OrderChannelRepositoryPort;
import com.pickeat.ports.out.OrderRepositoryPort;
import com.pickeat.ports.out.ParameterRepositoryPort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servicio de aplicacion para crear ordenes.
 */
@Service
public class CreateOrderService implements CreateOrderUseCase {
    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");
    private final OrderRepositoryPort orderRepository;
    private final MesaRepositoryPort mesaRepository;
    private final MenuItemRepositoryPort menuItemRepository;
    private final DiscountItemRepositoryPort discountItemRepository;
    private final ParameterRepositoryPort parameterRepository;
    private final OrderChannelRepositoryPort orderChannelRepository;
    private final CouponRepositoryPort couponRepository;

    public CreateOrderService(OrderRepositoryPort orderRepository,
                              MesaRepositoryPort mesaRepository,
                              MenuItemRepositoryPort menuItemRepository,
                              DiscountItemRepositoryPort discountItemRepository,
                              ParameterRepositoryPort parameterRepository,
                              OrderChannelRepositoryPort orderChannelRepository,
                              CouponRepositoryPort couponRepository) {
        this.orderRepository = orderRepository;
        this.mesaRepository = mesaRepository;
        this.menuItemRepository = menuItemRepository;
        this.discountItemRepository = discountItemRepository;
        this.parameterRepository = parameterRepository;
        this.orderChannelRepository = orderChannelRepository;
        this.couponRepository = couponRepository;
    }

    @Override
    public Order create(OrderDraft draft) {
        if (draft == null || draft.getMesaId() == null) {
            throw new IllegalArgumentException("La mesa es obligatoria.");
        }
        if (draft.getItems() == null || draft.getItems().isEmpty()) {
            throw new IllegalArgumentException("La orden debe incluir items.");
        }
        Mesa mesa = mesaRepository.findById(draft.getMesaId())
                .orElseThrow(() -> new IllegalArgumentException("La mesa no existe."));
        if (!mesa.isActive() || mesa.isDeleted()) {
            throw new IllegalArgumentException("La mesa no esta disponible.");
        }
        if (mesa.isOccupied()) {
            throw new IllegalArgumentException("La mesa ya esta ocupada.");
        }
        OrderChannel channel = resolveChannel(draft.getChannelId());

        Map<MenuItemId, Integer> quantities = mergeQuantities(draft.getItems());
        List<OrderItem> items = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal taxableSubtotal = BigDecimal.ZERO;
        Map<java.util.UUID, BigDecimal> itemTotals = new HashMap<>();
        Map<String, Integer> dishTypeCounts = new HashMap<>();
        for (Map.Entry<MenuItemId, Integer> entry : quantities.entrySet()) {
            int quantity = entry.getValue();
            if (quantity <= 0) {
                throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
            }
            MenuItem menuItem = menuItemRepository.findById(entry.getKey())
                    .orElseThrow(() -> new IllegalArgumentException("Item de menu no encontrado."));
            if (!menuItem.isActive() || menuItem.isDeleted()) {
                throw new IllegalArgumentException("El item de menu no esta disponible.");
            }
            BigDecimal unitPrice = menuItem.getPrice();
            BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
            lineTotal = lineTotal.setScale(2, RoundingMode.HALF_UP);
            items.add(new OrderItem(menuItem.getId(), quantity, unitPrice, lineTotal));
            subtotal = subtotal.add(lineTotal);
            if (menuItem.isApplyTax()) {
                taxableSubtotal = taxableSubtotal.add(lineTotal);
            }
            itemTotals.put(menuItem.getId().getValue(), lineTotal);
            dishTypeCounts.put(menuItem.getDishType().name(),
                    dishTypeCounts.getOrDefault(menuItem.getDishType().name(), 0) + quantity);
        }
        subtotal = subtotal.setScale(2, RoundingMode.HALF_UP);
        taxableSubtotal = taxableSubtotal.setScale(2, RoundingMode.HALF_UP);

        OrderConfig config = loadConfig();
        BigDecimal taxAmount = taxableSubtotal.multiply(config.getTaxRate().divide(ONE_HUNDRED, 4, RoundingMode.HALF_UP));
        taxAmount = taxAmount.setScale(2, RoundingMode.HALF_UP);

        BigDecimal baseForTip = subtotal.add(taxAmount);
        boolean tipEnabled = draft.getTipEnabled() == null || draft.getTipEnabled();
        BigDecimal tipAmount;
        if (!tipEnabled) {
            tipAmount = BigDecimal.ZERO;
        } else {
            TipType resolvedTipType = draft.getTipType() != null ? draft.getTipType() : config.getTipType();
            if (resolvedTipType == TipType.FIXED) {
                BigDecimal fixedValue = draft.getTipValue();
                if (fixedValue == null) {
                    throw new IllegalArgumentException("La propina fija es obligatoria.");
                }
                if (fixedValue.compareTo(BigDecimal.ZERO) < 0) {
                    throw new IllegalArgumentException("La propina fija no puede ser negativa.");
                }
                tipAmount = fixedValue;
            } else {
                tipAmount = baseForTip.multiply(config.getTipValue().divide(ONE_HUNDRED, 4, RoundingMode.HALF_UP));
            }
        }
        tipAmount = tipAmount.setScale(2, RoundingMode.HALF_UP);

        List<OrderDiscountDraft> discountDrafts = draft.getDiscountItems();
        Coupon coupon = resolveCoupon(draft.getCouponCode());
        if (coupon != null) {
            if (discountDrafts == null) {
                discountDrafts = new ArrayList<>();
            }
            discountDrafts.add(new OrderDiscountDraft(coupon.getDiscountItemId(), 1));
        }
        if ((discountDrafts == null || discountDrafts.isEmpty()) && coupon == null) {
            discountDrafts = autoApplyItemDiscounts(items, discountDrafts);
        }
        List<OrderDiscountItem> discountItems = resolveDiscountItems(discountDrafts, subtotal, taxAmount, tipAmount, itemTotals);
        BigDecimal baseTotal = subtotal.add(taxAmount).add(tipAmount);
        BigDecimal discountAmount = discountItems.stream()
                .map(OrderDiscountItem::getTotalValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (discountAmount.compareTo(baseTotal) > 0) {
            discountAmount = baseTotal;
        }
        discountAmount = discountAmount.setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalAmount = baseTotal.subtract(discountAmount);
        totalAmount = totalAmount.setScale(2, RoundingMode.HALF_UP);

        Order order = Order.createNew(
                mesa.getId(),
                channel.getId(),
                items,
                discountItems,
                subtotal,
                taxAmount,
                tipAmount,
                discountAmount,
                totalAmount,
                config.getCurrencyCode(),
                config.getCurrencySymbol(),
                draft.getBillingData()
        );
        mesa.setOccupied(true);
        mesaRepository.save(mesa);
        Order saved = orderRepository.save(order);
        if (coupon != null) {
            redeemCoupon(coupon, saved.getId());
        }
        generateCoupons(saved, dishTypeCounts);
        return saved;
    }

    private Map<MenuItemId, Integer> mergeQuantities(List<OrderItemDraft> items) {
        Map<MenuItemId, Integer> merged = new HashMap<>();
        for (OrderItemDraft draftItem : items) {
            if (draftItem == null || draftItem.getMenuItemId() == null) {
                continue;
            }
            int quantity = draftItem.getQuantity();
            merged.put(draftItem.getMenuItemId(), merged.getOrDefault(draftItem.getMenuItemId(), 0) + quantity);
        }
        return merged;
    }

    private List<OrderDiscountItem> resolveDiscountItems(List<OrderDiscountDraft> drafts,
                                                         BigDecimal subtotal,
                                                         BigDecimal taxAmount,
                                                         BigDecimal tipAmount,
                                                         Map<java.util.UUID, BigDecimal> itemTotals) {
        if (drafts == null || drafts.isEmpty()) {
            return List.of();
        }
        Map<com.pickeat.domain.DiscountItemId, Integer> quantities = mergeDiscountQuantities(drafts);
        BigDecimal baseTotal = subtotal.add(taxAmount).add(tipAmount);
        List<OrderDiscountItem> result = new ArrayList<>();
        boolean hasExclusive = false;
        for (Map.Entry<com.pickeat.domain.DiscountItemId, Integer> entry : quantities.entrySet()) {
            int quantity = entry.getValue();
            if (quantity <= 0) {
                throw new IllegalArgumentException("La cantidad de descuento debe ser mayor a cero.");
            }
            com.pickeat.domain.DiscountItem discountItem = discountItemRepository.findById(entry.getKey())
                    .orElseThrow(() -> new IllegalArgumentException("Descuento no encontrado."));
            if (!discountItem.isActive() || discountItem.isDeleted()) {
                throw new IllegalArgumentException("El descuento no esta disponible.");
            }
            if (discountItem.isExclusive() && quantities.size() > 1) {
                throw new IllegalArgumentException("Solo se permite un descuento exclusivo por orden.");
            }
            hasExclusive = hasExclusive || discountItem.isExclusive();
            BigDecimal unitValue = discountItem.getValue();
            BigDecimal lineTotal;
            BigDecimal baseForDiscount = baseTotal;
            if (discountItem.getApplyScope() == DiscountApplyScope.ITEM) {
                BigDecimal itemsTotal = BigDecimal.ZERO;
                for (java.util.UUID menuItemId : discountItem.getMenuItemIds()) {
                    itemsTotal = itemsTotal.add(itemTotals.getOrDefault(menuItemId, BigDecimal.ZERO));
                }
                if (itemsTotal.compareTo(BigDecimal.ZERO) <= 0) {
                    throw new IllegalArgumentException("El descuento no aplica a los items seleccionados.");
                }
                baseForDiscount = itemsTotal;
            }
            if (discountItem.isApplyOverDiscount()) {
                BigDecimal currentDiscount = result.stream()
                        .map(OrderDiscountItem::getTotalValue)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                baseForDiscount = baseForDiscount.subtract(currentDiscount).max(BigDecimal.ZERO);
            }
            if (discountItem.getDiscountType() == com.pickeat.domain.DiscountType.PERCENTAGE) {
                lineTotal = baseForDiscount.multiply(unitValue.divide(ONE_HUNDRED, 4, RoundingMode.HALF_UP))
                        .multiply(BigDecimal.valueOf(quantity));
            } else {
                lineTotal = unitValue.multiply(BigDecimal.valueOf(quantity));
            }
            lineTotal = lineTotal.setScale(2, RoundingMode.HALF_UP);
            result.add(new OrderDiscountItem(discountItem.getId(), quantity, discountItem.getDiscountType(), unitValue, lineTotal));
        }
        return result;
    }

    private Map<com.pickeat.domain.DiscountItemId, Integer> mergeDiscountQuantities(List<OrderDiscountDraft> items) {
        Map<com.pickeat.domain.DiscountItemId, Integer> merged = new HashMap<>();
        for (OrderDiscountDraft draftItem : items) {
            if (draftItem == null || draftItem.getDiscountItemId() == null) {
                continue;
            }
            int quantity = draftItem.getQuantity();
            merged.put(draftItem.getDiscountItemId(), merged.getOrDefault(draftItem.getDiscountItemId(), 0) + quantity);
        }
        return merged;
    }

    private OrderConfig loadConfig() {
        BigDecimal taxRate = parameterRepository.findByKey("TAX_RATE")
                .map(param -> param.getNumericValue())
                .orElse(BigDecimal.ZERO);
        String tipTypeText = parameterRepository.findByKey("TIP_TYPE")
                .map(param -> param.getTextValue())
                .orElse(TipType.PERCENTAGE.name());
        TipType tipType = TipType.PERCENTAGE;
        try {
            tipType = TipType.valueOf(tipTypeText);
        } catch (IllegalArgumentException ignored) {
            tipType = TipType.PERCENTAGE;
        }
        BigDecimal tipValue = parameterRepository.findByKey("TIP_VALUE")
                .map(param -> param.getNumericValue())
                .orElse(BigDecimal.ZERO);
        String currencyCode = parameterRepository.findByKey("CURRENCY_CODE")
                .map(param -> param.getTextValue())
                .orElse("USD");
        String currencySymbol = parameterRepository.findByKey("CURRENCY_SYMBOL")
                .map(param -> param.getTextValue())
                .orElse("$");
        return new OrderConfig(
                defaultIfNull(taxRate),
                tipType,
                defaultIfNull(tipValue),
                currencyCode,
                currencySymbol
        );
    }

    private BigDecimal defaultIfNull(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private OrderChannel resolveChannel(OrderChannelId channelId) {
        OrderChannel channel;
        if (channelId != null) {
            channel = orderChannelRepository.findById(channelId)
                    .orElseThrow(() -> new IllegalArgumentException("El canal no existe."));
        } else {
            channel = orderChannelRepository.findDefault()
                    .orElseGet(() -> orderChannelRepository.findByName("LOCAL")
                            .orElseThrow(() -> new IllegalArgumentException("No existe canal por defecto.")));
        }
        if (!channel.isActive() || channel.isDeleted()) {
            throw new IllegalArgumentException("El canal no esta disponible.");
        }
        return channel;
    }

    private Coupon resolveCoupon(String code) {
        if (code == null || code.isBlank()) {
            return null;
        }
        Coupon coupon = couponRepository.findByCode(code.trim())
                .orElseThrow(() -> new IllegalArgumentException("El cupon no existe."));
        if (coupon.getStatus() != CouponStatus.ACTIVE) {
            throw new IllegalArgumentException("El cupon no esta vigente.");
        }
        if (coupon.getExpiresAt().isBefore(Instant.now())) {
            coupon.setStatus(CouponStatus.EXPIRED);
            coupon.setUpdatedAt(Instant.now());
            couponRepository.save(coupon);
            throw new IllegalArgumentException("El cupon esta vencido.");
        }
        return coupon;
    }

    private void redeemCoupon(Coupon coupon, OrderId orderId) {
        coupon.setStatus(CouponStatus.REDEEMED);
        coupon.setRedeemedOrderId(orderId);
        coupon.setRedeemedAt(Instant.now());
        coupon.setUpdatedAt(Instant.now());
        couponRepository.save(coupon);
    }

    private List<OrderDiscountDraft> autoApplyItemDiscounts(List<OrderItem> items, List<OrderDiscountDraft> drafts) {
        List<DiscountItem> autoDiscounts = discountItemRepository.findAutoApplyItemDiscounts();
        if (autoDiscounts.isEmpty()) {
            return drafts;
        }
        List<OrderDiscountDraft> result = drafts == null ? new ArrayList<>() : new ArrayList<>(drafts);
        for (DiscountItem discountItem : autoDiscounts) {
            boolean matches = items.stream()
                    .anyMatch(item -> discountItem.getMenuItemIds().contains(item.getMenuItemId().getValue()));
            if (matches) {
                result.add(new OrderDiscountDraft(discountItem.getId(), 1));
            }
        }
        return result;
    }

    private void generateCoupons(Order order, Map<String, Integer> dishTypeCounts) {
        List<DiscountItem> generators = discountItemRepository.findCouponGenerators();
        if (generators.isEmpty()) {
            return;
        }
        List<Coupon> existingCoupons = couponRepository.findByGeneratedOrderId(order.getId());
        java.util.Set<com.pickeat.domain.DiscountItemId> existingDiscounts = existingCoupons.stream()
                .map(Coupon::getDiscountItemId)
                .collect(java.util.stream.Collectors.toSet());
        BigDecimal baseTotal = order.getSubtotal().add(order.getTaxAmount());
        boolean hasDiscounts = order.getDiscountAmount().compareTo(BigDecimal.ZERO) > 0 || !order.getDiscountItems().isEmpty();
        for (DiscountItem discountItem : generators) {
            if (!discountItem.isCouponActive()) {
                continue;
            }
            if (existingDiscounts.contains(discountItem.getId())) {
                continue;
            }
            if (discountItem.isCouponRequireNoDiscount() && hasDiscounts) {
                continue;
            }
            String rule = discountItem.getCouponRuleType();
            boolean qualifies = false;
            if ("MIN_TOTAL".equalsIgnoreCase(rule)) {
                BigDecimal min = discountItem.getCouponMinTotal() == null ? BigDecimal.ZERO : discountItem.getCouponMinTotal();
                qualifies = baseTotal.compareTo(min) >= 0;
            } else if ("MIN_ITEM_QTY_BY_DISH_TYPE".equalsIgnoreCase(rule)) {
                String dishType = discountItem.getCouponDishType();
                int minQty = discountItem.getCouponMinItemQty() == null ? 0 : discountItem.getCouponMinItemQty();
                qualifies = dishType != null && dishTypeCounts.getOrDefault(dishType, 0) >= minQty;
            }
            if (!qualifies) {
                continue;
            }
            int validityDays = discountItem.getCouponValidityDays() == null ? 7 : discountItem.getCouponValidityDays();
            Instant expiresAt = Instant.now().plusSeconds(validityDays * 86400L);
            Coupon coupon = Coupon.createNew(generateCouponCode(), discountItem.getId(), expiresAt, order.getId());
            couponRepository.save(coupon);
        }
    }

    private String generateCouponCode() {
        return java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
    }
}
