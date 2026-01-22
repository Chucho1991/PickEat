package com.pickeat.adapters.in.rest.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Respuesta REST de una orden.
 */
public class OrderResponse {
    private UUID id;
    private Long orderNumber;
    private UUID mesaId;
    private List<OrderItemResponse> items;
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal tipAmount;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;
    private String currencyCode;
    private String currencySymbol;
    private Instant createdAt;

    public OrderResponse(UUID id,
                         Long orderNumber,
                         UUID mesaId,
                         List<OrderItemResponse> items,
                         BigDecimal subtotal,
                         BigDecimal taxAmount,
                         BigDecimal tipAmount,
                         BigDecimal discountAmount,
                         BigDecimal totalAmount,
                         String currencyCode,
                         String currencySymbol,
                         Instant createdAt) {
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
    }

    public UUID getId() {
        return id;
    }

    public Long getOrderNumber() {
        return orderNumber;
    }

    public UUID getMesaId() {
        return mesaId;
    }

    public List<OrderItemResponse> getItems() {
        return items;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public BigDecimal getTipAmount() {
        return tipAmount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
