package com.pickeat.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * Entidad de dominio para ordenes.
 */
public class Order {
    private OrderId id;
    private Long orderNumber;
    private MesaId mesaId;
    private List<OrderItem> items;
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal tipAmount;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;
    private String currencyCode;
    private String currencySymbol;
    private Instant createdAt;
    private Instant updatedAt;

    public Order(OrderId id,
                 Long orderNumber,
                 MesaId mesaId,
                 List<OrderItem> items,
                 BigDecimal subtotal,
                 BigDecimal taxAmount,
                 BigDecimal tipAmount,
                 BigDecimal discountAmount,
                 BigDecimal totalAmount,
                 String currencyCode,
                 String currencySymbol,
                 Instant createdAt,
                 Instant updatedAt) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.mesaId = mesaId;
        this.items = items;
        this.subtotal = subtotal;
        this.taxAmount = taxAmount;
        this.tipAmount = tipAmount;
        this.discountAmount = discountAmount;
        this.totalAmount = totalAmount;
        this.currencyCode = currencyCode;
        this.currencySymbol = currencySymbol;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Order createNew(MesaId mesaId,
                                  List<OrderItem> items,
                                  BigDecimal subtotal,
                                  BigDecimal taxAmount,
                                  BigDecimal tipAmount,
                                  BigDecimal discountAmount,
                                  BigDecimal totalAmount,
                                  String currencyCode,
                                  String currencySymbol) {
        Instant now = Instant.now();
        return new Order(
                OrderId.newId(),
                null,
                mesaId,
                items,
                subtotal,
                taxAmount,
                tipAmount,
                discountAmount,
                totalAmount,
                currencyCode,
                currencySymbol,
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

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
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
