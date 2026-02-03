package com.pickeat.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Entidad de dominio para ordenes.
 */
public class Order {
    private OrderId id;
    private Long orderNumber;
    private MesaId mesaId;
    private OrderChannelId channelId;
    private List<OrderItem> items;
    private List<OrderDiscountItem> discountItems;
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal tipAmount;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;
    private String currencyCode;
    private String currencySymbol;
    private Map<String, String> billingData;
    private OrderStatus status;
    private boolean active;
    private boolean deleted;
    private Instant createdAt;
    private Instant updatedAt;

    public Order(OrderId id,
                 Long orderNumber,
                 MesaId mesaId,
                 OrderChannelId channelId,
                 List<OrderItem> items,
                 List<OrderDiscountItem> discountItems,
                 BigDecimal subtotal,
                 BigDecimal taxAmount,
                 BigDecimal tipAmount,
                 BigDecimal discountAmount,
                 BigDecimal totalAmount,
                 String currencyCode,
                 String currencySymbol,
                 Map<String, String> billingData,
                 OrderStatus status,
                 boolean active,
                 boolean deleted,
                 Instant createdAt,
                 Instant updatedAt) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.mesaId = mesaId;
        this.channelId = channelId;
        this.items = items;
        this.discountItems = discountItems;
        this.subtotal = subtotal;
        this.taxAmount = taxAmount;
        this.tipAmount = tipAmount;
        this.discountAmount = discountAmount;
        this.totalAmount = totalAmount;
        this.currencyCode = currencyCode;
        this.currencySymbol = currencySymbol;
        this.billingData = billingData == null ? new HashMap<>() : billingData;
        this.status = status;
        this.active = active;
        this.deleted = deleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Order createNew(MesaId mesaId,
                                  OrderChannelId channelId,
                                  List<OrderItem> items,
                                  List<OrderDiscountItem> discountItems,
                                  BigDecimal subtotal,
                                  BigDecimal taxAmount,
                                  BigDecimal tipAmount,
                                  BigDecimal discountAmount,
                                  BigDecimal totalAmount,
                                  String currencyCode,
                                  String currencySymbol,
                                  Map<String, String> billingData) {
        Instant now = Instant.now();
        return new Order(
                OrderId.newId(),
                null,
                mesaId,
                channelId,
                items,
                discountItems,
                subtotal,
                taxAmount,
                tipAmount,
                discountAmount,
                totalAmount,
                currencyCode,
                currencySymbol,
                billingData,
                OrderStatus.CREADA,
                true,
                false,
                now,
                now
        );
    }

    public OrderId getId() {
        return id;
    }

    public void setId(OrderId id) {
        this.id = id;
    }

    public Long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Long orderNumber) {
        this.orderNumber = orderNumber;
    }

    public MesaId getMesaId() {
        return mesaId;
    }

    public void setMesaId(MesaId mesaId) {
        this.mesaId = mesaId;
    }

    public OrderChannelId getChannelId() {
        return channelId;
    }

    public void setChannelId(OrderChannelId channelId) {
        this.channelId = channelId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public List<OrderDiscountItem> getDiscountItems() {
        return discountItems;
    }

    public void setDiscountItems(List<OrderDiscountItem> discountItems) {
        this.discountItems = discountItems;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getTipAmount() {
        return tipAmount;
    }

    public void setTipAmount(BigDecimal tipAmount) {
        this.tipAmount = tipAmount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public Map<String, String> getBillingData() {
        return billingData;
    }

    public void setBillingData(Map<String, String> billingData) {
        this.billingData = billingData == null ? new HashMap<>() : billingData;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
