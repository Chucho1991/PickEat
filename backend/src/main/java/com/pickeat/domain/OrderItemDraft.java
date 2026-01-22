package com.pickeat.domain;

/**
 * Item solicitado para crear una orden.
 */
public class OrderItemDraft {
    private MenuItemId menuItemId;
    private int quantity;

    public OrderItemDraft(MenuItemId menuItemId, int quantity) {
        this.menuItemId = menuItemId;
        this.quantity = quantity;
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
}
