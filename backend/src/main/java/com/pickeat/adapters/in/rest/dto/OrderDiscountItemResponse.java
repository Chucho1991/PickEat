package com.pickeat.adapters.in.rest.dto;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Descuento aplicado a una orden en respuestas REST.
 */
public class OrderDiscountItemResponse {
    private UUID discountItemId;
    private int quantity;
    private String discountType;
    private BigDecimal unitValue;
    private BigDecimal totalValue;

    public OrderDiscountItemResponse(UUID discountItemId,
                                     int quantity,
                                     String discountType,
                                     BigDecimal unitValue,
                                     BigDecimal totalValue) {
        this.discountItemId = discountItemId;
        this.quantity = quantity;
        this.discountType = discountType;
        this.unitValue = unitValue;
        this.totalValue = totalValue;
    }

    public UUID getDiscountItemId() {
        return discountItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getDiscountType() {
        return discountType;
    }

    public BigDecimal getUnitValue() {
        return unitValue;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }
}
