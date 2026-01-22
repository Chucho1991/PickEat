package com.pickeat.adapters.in.rest.dto;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Item de una orden en respuestas REST.
 */
public class OrderItemResponse {
    private UUID menuItemId;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;

    public OrderItemResponse(UUID menuItemId, int quantity, BigDecimal unitPrice, BigDecimal totalPrice) {
        this.menuItemId = menuItemId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
    }

    public UUID getMenuItemId() {
        return menuItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
}
