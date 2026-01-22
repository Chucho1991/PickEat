package com.pickeat.domain;

import java.math.BigDecimal;

/**
 * Parametro de configuracion.
 */
public class AppParameter {
    private String key;
    private BigDecimal numericValue;
    private String textValue;
    private Boolean booleanValue;

    public AppParameter(String key, BigDecimal numericValue, String textValue, Boolean booleanValue) {
        this.key = key;
        this.numericValue = numericValue;
        this.textValue = textValue;
        this.booleanValue = booleanValue;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public BigDecimal getNumericValue() {
        return numericValue;
    }

    public void setNumericValue(BigDecimal numericValue) {
        this.numericValue = numericValue;
    }

    public String getTextValue() {
        return textValue;
    }

    public void setTextValue(String textValue) {
        this.textValue = textValue;
    }

    public Boolean getBooleanValue() {
        return booleanValue;
    }

    public void setBooleanValue(Boolean booleanValue) {
        this.booleanValue = booleanValue;
    }
}
