package com.pickeat.domain;

import java.util.List;
import java.util.Map;

/**
 * Datos base para crear una orden.
 */
public class OrderDraft {
    private MesaId mesaId;
    private List<OrderItemDraft> items;
    private List<OrderDiscountDraft> discountItems;
    private OrderChannelId channelId;
    private TipType tipType;
    private java.math.BigDecimal tipValue;
    private Boolean tipEnabled;
    private Map<String, String> billingData;
    private String couponCode;

    public OrderDraft(MesaId mesaId,
                      List<OrderItemDraft> items,
                      List<OrderDiscountDraft> discountItems,
                      OrderChannelId channelId,
                      TipType tipType,
                      java.math.BigDecimal tipValue,
                      Boolean tipEnabled,
                      Map<String, String> billingData,
                      String couponCode) {
        this.mesaId = mesaId;
        this.items = items;
        this.discountItems = discountItems;
        this.channelId = channelId;
        this.tipType = tipType;
        this.tipValue = tipValue;
        this.tipEnabled = tipEnabled;
        this.billingData = billingData;
        this.couponCode = couponCode;
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

    public List<OrderDiscountDraft> getDiscountItems() {
        return discountItems;
    }

    public void setDiscountItems(List<OrderDiscountDraft> discountItems) {
        this.discountItems = discountItems;
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

    public Map<String, String> getBillingData() {
        return billingData;
    }

    public void setBillingData(Map<String, String> billingData) {
        this.billingData = billingData;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }
}
