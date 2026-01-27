package com.pickeat.adapters.in.rest.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Solicitud para crear una orden.
 */
public class OrderCreateRequest {
    @NotNull(message = "La mesa es obligatoria.")
    private UUID mesaId;

    private UUID channelId;

    @NotEmpty(message = "Debe agregar al menos un item.")
    @Valid
    private List<OrderItemRequest> items;

    @Valid
    private List<OrderDiscountItemRequest> discountItems;

    private String tipType;

    @DecimalMin(value = "0.00", inclusive = true, message = "La propina debe ser mayor o igual a 0.")
    private BigDecimal tipValue;

    private Boolean tipEnabled;

    public UUID getMesaId() {
        return mesaId;
    }

    public void setMesaId(UUID mesaId) {
        this.mesaId = mesaId;
    }

    public UUID getChannelId() {
        return channelId;
    }

    public void setChannelId(UUID channelId) {
        this.channelId = channelId;
    }

    public List<OrderItemRequest> getItems() {
        return items;
    }

    public void setItems(List<OrderItemRequest> items) {
        this.items = items;
    }

    public List<OrderDiscountItemRequest> getDiscountItems() {
        return discountItems;
    }

    public void setDiscountItems(List<OrderDiscountItemRequest> discountItems) {
        this.discountItems = discountItems;
    }

    public String getTipType() {
        return tipType;
    }

    public void setTipType(String tipType) {
        this.tipType = tipType;
    }

    public BigDecimal getTipValue() {
        return tipValue;
    }

    public void setTipValue(BigDecimal tipValue) {
        this.tipValue = tipValue;
    }

    public Boolean getTipEnabled() {
        return tipEnabled;
    }

    public void setTipEnabled(Boolean tipEnabled) {
        this.tipEnabled = tipEnabled;
    }
}
