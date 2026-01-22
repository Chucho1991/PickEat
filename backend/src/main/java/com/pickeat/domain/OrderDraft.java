package com.pickeat.domain;

import java.util.List;

/**
 * Datos base para crear una orden.
 */
public class OrderDraft {
    private MesaId mesaId;
    private List<OrderItemDraft> items;

    public OrderDraft(MesaId mesaId, List<OrderItemDraft> items) {
        this.mesaId = mesaId;
        this.items = items;
    }

    public MesaId getMesaId() {
        return mesaId;
    }

    public void setMesaId(MesaId mesaId) {
        this.mesaId = mesaId;
    }

    public List<OrderItemDraft> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDraft> items) {
        this.items = items;
    }
}
