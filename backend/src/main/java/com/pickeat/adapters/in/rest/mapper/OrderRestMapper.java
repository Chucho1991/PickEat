package com.pickeat.adapters.in.rest.mapper;

import com.pickeat.adapters.in.rest.dto.OrderConfigResponse;
import com.pickeat.adapters.in.rest.dto.OrderCreateRequest;
import com.pickeat.adapters.in.rest.dto.OrderItemRequest;
import com.pickeat.adapters.in.rest.dto.OrderDiscountItemRequest;
import com.pickeat.adapters.in.rest.dto.OrderDiscountItemResponse;
import com.pickeat.adapters.in.rest.dto.OrderItemResponse;
import com.pickeat.adapters.in.rest.dto.OrderResponse;
import com.pickeat.domain.DiscountItemId;
import com.pickeat.domain.MenuItemId;
import com.pickeat.domain.MesaId;
import com.pickeat.domain.Order;
import com.pickeat.domain.OrderChannelId;
import com.pickeat.domain.OrderConfig;
import com.pickeat.domain.OrderDraft;
import com.pickeat.domain.OrderDiscountDraft;
import com.pickeat.domain.OrderDiscountItem;
import com.pickeat.domain.OrderItem;
import com.pickeat.domain.OrderItemDraft;
import com.pickeat.domain.TipType;

import java.util.Collections;
import java.util.List;

/**
 * Mapper REST para ordenes.
 */
public class OrderRestMapper {
    public OrderDraft toDraft(OrderCreateRequest request) {
        List<OrderItemDraft> items = request.getItems().stream()
                .map(this::toItemDraft)
                .toList();
        List<OrderDiscountDraft> discountItems = request.getDiscountItems() == null
                ? Collections.emptyList()
                : request.getDiscountItems().stream()
                        .map(this::toDiscountDraft)
                        .toList();
        TipType tipType = null;
        if (request.getTipType() != null) {
            try {
                tipType = TipType.valueOf(request.getTipType());
            } catch (IllegalArgumentException ignored) {
                tipType = null;
            }
        }
        OrderChannelId channelId = request.getChannelId() != null ? new OrderChannelId(request.getChannelId()) : null;
        return new OrderDraft(
                new MesaId(request.getMesaId()),
                items,
                discountItems,
                channelId,
                tipType,
                request.getTipValue(),
                request.getTipEnabled(),
                request.getBillingData(),
                request.getCouponCode()
        );
    }

    public OrderResponse toResponse(Order order) {
        List<OrderItemResponse> items = order.getItems().stream()
                .map(this::toItemResponse)
                .toList();
        List<OrderDiscountItemResponse> discountItems = order.getDiscountItems() == null
                ? Collections.emptyList()
                : order.getDiscountItems().stream()
                        .map(this::toDiscountResponse)
                        .toList();
        return new OrderResponse(
                order.getId().getValue(),
                order.getOrderNumber(),
                order.getMesaId().getValue(),
                order.getChannelId().getValue(),
                items,
                discountItems,
                order.getSubtotal(),
                order.getTaxAmount(),
                order.getTipAmount(),
                order.getDiscountAmount(),
                order.getTotalAmount(),
                order.getCurrencyCode(),
                order.getCurrencySymbol(),
                order.getBillingData(),
                order.getStatus().name(),
                order.isActive(),
                order.isDeleted(),
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

    private OrderDiscountDraft toDiscountDraft(OrderDiscountItemRequest request) {
        return new OrderDiscountDraft(new DiscountItemId(request.getDiscountItemId()), request.getQuantity());
    }

    private OrderItemResponse toItemResponse(OrderItem item) {
        return new OrderItemResponse(
                item.getMenuItemId().getValue(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getTotalPrice()
        );
    }

    private OrderDiscountItemResponse toDiscountResponse(OrderDiscountItem item) {
        return new OrderDiscountItemResponse(
                item.getDiscountItemId().getValue(),
                item.getQuantity(),
                item.getDiscountType().name(),
                item.getUnitValue(),
                item.getTotalValue()
        );
    }
}
