package com.pickeat.adapters.in.rest.mapper;

import com.pickeat.adapters.in.rest.dto.OrderConfigResponse;
import com.pickeat.adapters.in.rest.dto.OrderCreateRequest;
import com.pickeat.adapters.in.rest.dto.OrderItemRequest;
import com.pickeat.adapters.in.rest.dto.OrderItemResponse;
import com.pickeat.adapters.in.rest.dto.OrderResponse;
import com.pickeat.domain.MenuItemId;
import com.pickeat.domain.MesaId;
import com.pickeat.domain.Order;
import com.pickeat.domain.OrderConfig;
import com.pickeat.domain.OrderDraft;
import com.pickeat.domain.OrderItem;
import com.pickeat.domain.OrderItemDraft;

import java.util.List;

/**
 * Mapper REST para ordenes.
 */
public class OrderRestMapper {
    public OrderDraft toDraft(OrderCreateRequest request) {
        List<OrderItemDraft> items = request.getItems().stream()
                .map(this::toItemDraft)
                .toList();
        return new OrderDraft(new MesaId(request.getMesaId()), items);
    }

    public OrderResponse toResponse(Order order) {
        List<OrderItemResponse> items = order.getItems().stream()
                .map(this::toItemResponse)
                .toList();
        return new OrderResponse(
                order.getId().getValue(),
                order.getOrderNumber(),
                order.getMesaId().getValue(),
                items,
                order.getSubtotal(),
                order.getTaxAmount(),
                order.getTipAmount(),
                order.getDiscountAmount(),
                order.getTotalAmount(),
                order.getCurrencyCode(),
                order.getCurrencySymbol(),
                order.getCreatedAt()
        );
    }

    public OrderConfigResponse toConfigResponse(OrderConfig config) {
        return new OrderConfigResponse(
                config.getTaxRate(),
                config.getTipType().name(),
                config.getTipValue(),
                config.getCurrencyCode(),
                config.getCurrencySymbol()
        );
    }

    private OrderItemDraft toItemDraft(OrderItemRequest request) {
        return new OrderItemDraft(new MenuItemId(request.getMenuItemId()), request.getQuantity());
    }

    private OrderItemResponse toItemResponse(OrderItem item) {
        return new OrderItemResponse(
                item.getMenuItemId().getValue(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getTotalPrice()
        );
    }
}
