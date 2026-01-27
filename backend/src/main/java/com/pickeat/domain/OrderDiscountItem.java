package com.pickeat.domain;

import java.math.BigDecimal;

/**
 * Descuento aplicado a una orden.
 */
public class OrderDiscountItem {
    private DiscountItemId discountItemId;
    private int quantity;
    private DiscountType discountType;
    private BigDecimal unitValue;
    private BigDecimal totalValue;

    public OrderDiscountItem(DiscountItemId discountItemId,
                             int quantity,
                             DiscountType discountType,
                             BigDecimal unitValue,
                             BigDecimal totalValue) {
        this.discountItemId = discountItemId;
        this.quantity = quantity;
        this.discountType = discountType;
        this.unitValue = unitValue;
        this.totalValue = totalValue;
    }

    public DiscountItemId getDiscountItemId() {
        return discountItemId;
    }

    public void setDiscountItemId(DiscountItemId discountItemId) {
        this.discountItemId = discountItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getUnitValue() {
        return unitValue;
    }

    public void setUnitValue(BigDecimal unitValue) {
        this.unitValue = unitValue;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }
}
