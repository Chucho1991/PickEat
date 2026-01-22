package com.pickeat.domain;

import java.math.BigDecimal;

/**
 * Configuracion usada para calcular ordenes.
 */
public class OrderConfig {
    private BigDecimal taxRate;
    private TipType tipType;
    private BigDecimal tipValue;
    private String currencyCode;
    private String currencySymbol;

    public OrderConfig(BigDecimal taxRate, TipType tipType, BigDecimal tipValue, String currencyCode, String currencySymbol) {
        this.taxRate = taxRate;
        this.tipType = tipType;
        this.tipValue = tipValue;
        this.currencyCode = currencyCode;
        this.currencySymbol = currencySymbol;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public TipType getTipType() {
        return tipType;
    }

    public void setTipType(TipType tipType) {
        this.tipType = tipType;
    }

    public BigDecimal getTipValue() {
        return tipValue;
    }

    public void setTipValue(BigDecimal tipValue) {
        this.tipValue = tipValue;
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
}
