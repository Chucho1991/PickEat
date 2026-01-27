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
    private UUID channelId;
    private List<OrderItemResponse> items;
    private List<OrderDiscountItemResponse> discountItems;
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal tipAmount;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;
    private String currencyCode;
    private String currencySymbol;
    private String status;
    private boolean activo;
    private boolean deleted;
    private Instant createdAt;

    public OrderResponse(UUID id,
                         Long orderNumber,
                         UUID mesaId,
                         UUID channelId,
                         List<OrderItemResponse> items,
                         List<OrderDiscountItemResponse> discountItems,
                         BigDecimal subtotal,
                         BigDecimal taxAmount,
                         BigDecimal tipAmount,
                         BigDecimal discountAmount,
                         BigDecimal totalAmount,
                         String currencyCode,
                         String currencySymbol,
                         String status,
                         boolean activo,
                         boolean deleted,
                         Instant createdAt) {
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
        this.status = status;
        this.activo = activo;
        this.deleted = deleted;
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

    public UUID getChannelId() {
        return channelId;
    }

    public List<OrderItemResponse> getItems() {
        return items;
    }

    public List<OrderDiscountItemResponse> getDiscountItems() {
        return discountItems;
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

    public String getStatus() {
        return status;
    }

    public boolean isActivo() {
        return activo;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
