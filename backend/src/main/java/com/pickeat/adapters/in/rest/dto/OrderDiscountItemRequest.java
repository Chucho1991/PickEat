package com.pickeat.adapters.in.rest.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Descuento solicitado para una orden.
 */
public class OrderDiscountItemRequest {
    @NotNull(message = "El descuento es obligatorio.")
    private UUID discountItemId;

    @NotNull(message = "La cantidad es obligatoria.")
    @Min(value = 1, message = "La cantidad debe ser mayor a cero.")
    private Integer quantity;

    public UUID getDiscountItemId() {
        return discountItemId;
    }

    public void setDiscountItemId(UUID discountItemId) {
        this.discountItemId = discountItemId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
