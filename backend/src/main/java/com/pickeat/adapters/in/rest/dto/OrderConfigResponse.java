package com.pickeat.adapters.in.rest.dto;

import java.math.BigDecimal;

/**
 * Respuesta REST con configuracion de ordenes.
 */
public class OrderConfigResponse {
    private BigDecimal taxRate;
    private String tipType;
    private BigDecimal tipValue;
    private String currencyCode;
    private String currencySymbol;

    public OrderConfigResponse(BigDecimal taxRate,
                               String tipType,
                               BigDecimal tipValue,
                               String currencyCode,
                               String currencySymbol) {
        this.taxRate = taxRate;
        this.tipType = tipType;
        this.tipValue = tipValue;
        this.currencyCode = currencyCode;
        this.currencySymbol = currencySymbol;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public String getTipType() {
        return tipType;
    }

    public BigDecimal getTipValue() {
        return tipValue;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }
}
