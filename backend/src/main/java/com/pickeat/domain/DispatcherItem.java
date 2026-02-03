package com.pickeat.domain;

import java.time.Instant;

/**
 * Representa un plato visible en el tablero de despachador.
 */
public class DispatcherItem {
    private String orderId;
    private String mesa;
    private String plato;
    private int cantidad;
    private Instant addedAt;

    public DispatcherItem(String orderId, String mesa, String plato, int cantidad, Instant addedAt) {
        this.orderId = orderId;
        this.mesa = mesa;
        this.plato = plato;
        this.cantidad = cantidad;
        this.addedAt = addedAt;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMesa() {
        return mesa;
    }

    public void setMesa(String mesa) {
        this.mesa = mesa;
    }

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
}
