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
import com.pickeat.domain.OrderItem;
import com.pickeat.domain.OrderItemDraft;
import com.pickeat.domain.TipType;
import com.pickeat.ports.in.CreateOrderUseCase;
import com.pickeat.ports.out.MenuItemRepositoryPort;
import com.pickeat.ports.out.MesaRepositoryPort;
import com.pickeat.ports.out.OrderChannelRepositoryPort;
import com.pickeat.ports.out.OrderRepositoryPort;
import com.pickeat.ports.out.ParameterRepositoryPort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    private final ParameterRepositoryPort parameterRepository;
    private final OrderChannelRepositoryPort orderChannelRepository;

    public CreateOrderService(OrderRepositoryPort orderRepository,
                              MesaRepositoryPort mesaRepository,
                              MenuItemRepositoryPort menuItemRepository,
                              ParameterRepositoryPort parameterRepository,
                              OrderChannelRepositoryPort orderChannelRepository) {
        this.orderRepository = orderRepository;
        this.mesaRepository = mesaRepository;
        this.menuItemRepository = menuItemRepository;
        this.parameterRepository = parameterRepository;
        this.orderChannelRepository = orderChannelRepository;
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

        BigDecimal discountAmount = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalAmount = subtotal.add(taxAmount).add(tipAmount).subtract(discountAmount);
        totalAmount = totalAmount.setScale(2, RoundingMode.HALF_UP);

        Order order = Order.createNew(
                mesa.getId(),
                channel.getId(),
                items,
                subtotal,
                taxAmount,
                tipAmount,
                discountAmount,
                totalAmount,
                config.getCurrencyCode(),
                config.getCurrencySymbol()
        );
        mesa.setOccupied(true);
        mesaRepository.save(mesa);
        return orderRepository.save(order);
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
}
