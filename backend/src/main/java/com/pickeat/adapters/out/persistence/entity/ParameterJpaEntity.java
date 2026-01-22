package com.pickeat.adapters.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Entidad JPA para parametros de configuracion.
 */
@Entity
@Table(name = "app_parameter")
public class ParameterJpaEntity {
    @Id
    @Column(name = "param_key", nullable = false, length = 60)
    private String key;

    @Column(name = "numeric_value", precision = 10, scale = 2)
    private BigDecimal numericValue;

    @Column(name = "text_value", length = 80)
    private String textValue;

    @Column(name = "boolean_value")
    private Boolean booleanValue;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

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

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
