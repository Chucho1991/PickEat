package com.pickeat.domain;

/**
 * Item de descuento solicitado para una orden.
 */
public class OrderDiscountDraft {
    private DiscountItemId discountItemId;
    private int quantity;

    public OrderDiscountDraft(DiscountItemId discountItemId, int quantity) {
        this.discountItemId = discountItemId;
        this.quantity = quantity;
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
}
