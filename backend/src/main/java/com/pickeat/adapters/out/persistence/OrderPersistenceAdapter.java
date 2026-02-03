package com.pickeat.adapters.out.persistence;

import com.pickeat.adapters.out.persistence.entity.OrderDiscountItemJpaEntity;
import com.pickeat.adapters.out.persistence.entity.OrderItemJpaEntity;
import com.pickeat.adapters.out.persistence.entity.OrderJpaEntity;
import com.pickeat.adapters.out.persistence.repository.OrderJpaRepository;
import com.pickeat.domain.DiscountItemId;
import com.pickeat.domain.MenuItemId;
import com.pickeat.domain.MesaId;
import com.pickeat.domain.Order;
import com.pickeat.domain.OrderDiscountItem;
import com.pickeat.domain.OrderId;
import com.pickeat.domain.OrderItem;
import com.pickeat.domain.OrderStatus;
import com.pickeat.ports.out.OrderRepositoryPort;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

/**
 * Adaptador de persistencia para ordenes.
 */
@Component
public class OrderPersistenceAdapter implements OrderRepositoryPort {
    private final OrderJpaRepository repository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public OrderPersistenceAdapter(OrderJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Order save(Order order) {
        OrderJpaEntity entity = toEntity(order);
        OrderJpaEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    @Override
    public List<Order> findAll() {
        return repository.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Optional<Order> findById(OrderId id) {
        return repository.findById(id.getValue()).map(this::toDomain);
    }

    private OrderJpaEntity toEntity(Order order) {
        OrderJpaEntity entity = new OrderJpaEntity();
        entity.setId(order.getId().getValue());
        entity.setOrderNumber(order.getOrderNumber());
        entity.setMesaId(order.getMesaId().getValue());
        entity.setChannelId(order.getChannelId().getValue());
        entity.setSubtotal(order.getSubtotal());
        entity.setTaxAmount(order.getTaxAmount());
        entity.setTipAmount(order.getTipAmount());
        entity.setDiscountAmount(order.getDiscountAmount());
        entity.setTotalAmount(order.getTotalAmount());
        entity.setCurrencyCode(order.getCurrencyCode());
        entity.setCurrencySymbol(order.getCurrencySymbol());
        entity.setBillingData(writeBillingData(order.getBillingData()));
        entity.setStatus(order.getStatus().name());
        entity.setActive(order.isActive());
        entity.setDeleted(order.isDeleted());
        entity.setCreatedAt(order.getCreatedAt());
        entity.setUpdatedAt(order.getUpdatedAt());

        List<OrderItemJpaEntity> items = order.getItems().stream()
                .map(item -> toItemEntity(entity, item))
                .toList();
        entity.setItems(new java.util.ArrayList<>(items));
        List<OrderDiscountItem> orderDiscountItems = order.getDiscountItems() == null ? List.of() : order.getDiscountItems();
        List<OrderDiscountItemJpaEntity> discountItems = orderDiscountItems.stream()
                .map(item -> toDiscountEntity(entity, item))
                .toList();
        entity.setDiscountItems(new java.util.ArrayList<>(discountItems));
        return entity;
    }

    private OrderItemJpaEntity toItemEntity(OrderJpaEntity orderEntity, OrderItem item) {
        OrderItemJpaEntity entity = new OrderItemJpaEntity();
        entity.setId(UUID.randomUUID());
        entity.setOrder(orderEntity);
        entity.setMenuItemId(item.getMenuItemId().getValue());
        entity.setQuantity(item.getQuantity());
        entity.setUnitPrice(item.getUnitPrice());
        entity.setTotalPrice(item.getTotalPrice());
        return entity;
    }

    private OrderDiscountItemJpaEntity toDiscountEntity(OrderJpaEntity orderEntity, OrderDiscountItem item) {
        OrderDiscountItemJpaEntity entity = new OrderDiscountItemJpaEntity();
        entity.setId(UUID.randomUUID());
        entity.setOrder(orderEntity);
        entity.setDiscountItemId(item.getDiscountItemId().getValue());
        entity.setQuantity(item.getQuantity());
        entity.setDiscountType(item.getDiscountType());
        entity.setUnitValue(item.getUnitValue());
        entity.setTotalValue(item.getTotalValue());
        return entity;
    }

    private Order toDomain(OrderJpaEntity entity) {
        List<OrderItem> items = entity.getItems().stream()
                .map(item -> new OrderItem(
                        new MenuItemId(item.getMenuItemId()),
                        item.getQuantity(),
                        item.getUnitPrice(),
                        item.getTotalPrice()
                ))
                .toList();
        List<OrderDiscountItem> discountItems = entity.getDiscountItems().stream()
                .map(item -> new OrderDiscountItem(
                        new DiscountItemId(item.getDiscountItemId()),
                        item.getQuantity(),
                        item.getDiscountType(),
                        item.getUnitValue(),
                        item.getTotalValue()
                ))
                .toList();
        return new Order(
                new OrderId(entity.getId()),
                entity.getOrderNumber(),
                new MesaId(entity.getMesaId()),
                new com.pickeat.domain.OrderChannelId(entity.getChannelId()),
                items,
                discountItems,
                entity.getSubtotal(),
                entity.getTaxAmount(),
                entity.getTipAmount(),
                entity.getDiscountAmount(),
                entity.getTotalAmount(),
                entity.getCurrencyCode(),
                entity.getCurrencySymbol(),
                readBillingData(entity.getBillingData()),
                parseStatus(entity.getStatus()),
                entity.isActive(),
                entity.isDeleted(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    /**
     * Convierte el valor persistido a enum de estado.
     *
     * @param status valor persistido.
     * @return estado resuelto.
     */
    private OrderStatus parseStatus(String status) {
        if (status == null || status.isBlank()) {
            return OrderStatus.CREADA;
        }
        try {
            return OrderStatus.valueOf(status);
        } catch (IllegalArgumentException ignored) {
            return OrderStatus.CREADA;
        }
    }

    private String writeBillingData(Map<String, String> billingData) {
        try {
            return objectMapper.writeValueAsString(billingData == null ? Map.of() : billingData);
        } catch (Exception ignored) {
            return "{}";
        }
    }

    private Map<String, String> readBillingData(String raw) {
        if (raw == null || raw.isBlank()) {
            return new HashMap<>();
        }
        try {
            return objectMapper.readValue(raw, new TypeReference<Map<String, String>>() {});
        } catch (Exception ignored) {
            return new HashMap<>();
        }
    }
}
