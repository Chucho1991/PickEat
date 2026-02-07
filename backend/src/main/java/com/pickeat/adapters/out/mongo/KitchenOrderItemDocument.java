package com.pickeat.adapters.out.mongo;

import java.time.Instant;

/**
 * Documento embebido con el detalle de un plato en una orden de cocina.
 */
public class KitchenOrderItemDocument {
    private String plato;
    private int cantidad;
    private Instant addedAt;
    private Instant updatedAt;

    public String getPlato() {
        return plato;
    }

    public void setPlato(String plato) {
        this.plato = plato;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Instant getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(Instant addedAt) {
        this.addedAt = addedAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
