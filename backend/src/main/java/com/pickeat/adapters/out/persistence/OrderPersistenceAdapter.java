package com.pickeat.adapters.out.persistence;

import com.pickeat.adapters.out.persistence.entity.OrderItemJpaEntity;
import com.pickeat.adapters.out.persistence.entity.OrderJpaEntity;
import com.pickeat.adapters.out.persistence.repository.OrderJpaRepository;
import com.pickeat.domain.MenuItemId;
import com.pickeat.domain.MesaId;
import com.pickeat.domain.Order;
import com.pickeat.domain.OrderId;
import com.pickeat.domain.OrderItem;
import com.pickeat.ports.out.OrderRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Adaptador de persistencia para ordenes.
 */
@Component
public class OrderPersistenceAdapter implements OrderRepositoryPort {
    private final OrderJpaRepository repository;

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
    public Optional<Order> findById(OrderId id) {
        return repository.findById(id.getValue()).map(this::toDomain);
    }

    private OrderJpaEntity toEntity(Order order) {
        OrderJpaEntity entity = new OrderJpaEntity();
        entity.setId(order.getId().getValue());
        entity.setOrderNumber(order.getOrderNumber());
        entity.setMesaId(order.getMesaId().getValue());
        entity.setSubtotal(order.getSubtotal());
        entity.setTaxAmount(order.getTaxAmount());
        entity.setTipAmount(order.getTipAmount());
        entity.setDiscountAmount(order.getDiscountAmount());
        entity.setTotalAmount(order.getTotalAmount());
        entity.setCurrencyCode(order.getCurrencyCode());
        entity.setCurrencySymbol(order.getCurrencySymbol());
        entity.setCreatedAt(order.getCreatedAt());
        entity.setUpdatedAt(order.getUpdatedAt());

        List<OrderItemJpaEntity> items = order.getItems().stream()
                .map(item -> toItemEntity(entity, item))
                .toList();
        entity.setItems(new java.util.ArrayList<>(items));
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

    private Order toDomain(OrderJpaEntity entity) {
        List<OrderItem> items = entity.getItems().stream()
                .map(item -> new OrderItem(
                        new MenuItemId(item.getMenuItemId()),
                        item.getQuantity(),
                        item.getUnitPrice(),
                        item.getTotalPrice()
                ))
                .toList();
        return new Order(
                new OrderId(entity.getId()),
                entity.getOrderNumber(),
                new MesaId(entity.getMesaId()),
                items,
                entity.getSubtotal(),
                entity.getTaxAmount(),
                entity.getTipAmount(),
                entity.getDiscountAmount(),
                entity.getTotalAmount(),
                entity.getCurrencyCode(),
                entity.getCurrencySymbol(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
