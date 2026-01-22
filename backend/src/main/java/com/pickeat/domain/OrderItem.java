package com.pickeat.domain;

import java.math.BigDecimal;

/**
 * Item agregado a una orden.
 */
public class OrderItem {
    private MenuItemId menuItemId;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;

    public OrderItem(MenuItemId menuItemId, int quantity, BigDecimal unitPrice, BigDecimal totalPrice) {
        this.menuItemId = menuItemId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
    }

    public MenuItemId getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(MenuItemId menuItemId) {
        this.menuItemId = menuItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
