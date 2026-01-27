package com.pickeat.adapters.in.rest.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Solicitud para actualizar configuracion de ordenes.
 */
public class OrderConfigRequest {
    @NotNull
    @DecimalMin("0.00")
    @DecimalMax("100.00")
    private BigDecimal taxRate;

    @NotNull
    @DecimalMin("0.00")
    @DecimalMax("100.00")
    private BigDecimal tipValue;

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public BigDecimal getTipValue() {
        return tipValue;
    }

    public void setTipValue(BigDecimal tipValue) {
        this.tipValue = tipValue;
    }
}
