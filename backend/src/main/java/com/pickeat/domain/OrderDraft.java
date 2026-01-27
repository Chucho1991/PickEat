package com.pickeat.domain;

import java.util.List;

/**
 * Datos base para crear una orden.
 */
public class OrderDraft {
    private MesaId mesaId;
    private List<OrderItemDraft> items;
    private OrderChannelId channelId;
    private TipType tipType;
    private java.math.BigDecimal tipValue;
    private Boolean tipEnabled;

    public OrderDraft(MesaId mesaId,
                      List<OrderItemDraft> items,
                      OrderChannelId channelId,
                      TipType tipType,
                      java.math.BigDecimal tipValue,
                      Boolean tipEnabled) {
        this.mesaId = mesaId;
        this.items = items;
        this.channelId = channelId;
        this.tipType = tipType;
        this.tipValue = tipValue;
        this.tipEnabled = tipEnabled;
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

    public OrderChannelId getChannelId() {
        return channelId;
    }

    public void setChannelId(OrderChannelId channelId) {
        this.channelId = channelId;
    }

    public TipType getTipType() {
        return tipType;
    }

    public void setTipType(TipType tipType) {
        this.tipType = tipType;
    }

    public java.math.BigDecimal getTipValue() {
        return tipValue;
    }

    public void setTipValue(java.math.BigDecimal tipValue) {
        this.tipValue = tipValue;
    }

    public Boolean getTipEnabled() {
        return tipEnabled;
    }

    public void setTipEnabled(Boolean tipEnabled) {
        this.tipEnabled = tipEnabled;
    }
}
