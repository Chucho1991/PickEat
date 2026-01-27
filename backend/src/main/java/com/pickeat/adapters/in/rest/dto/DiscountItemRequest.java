package com.pickeat.adapters.in.rest.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Solicitud para crear o actualizar un descuento.
 */
public class DiscountItemRequest {
    @NotBlank(message = "La descripcion larga es obligatoria.")
    private String longDescription;

    @NotBlank(message = "La descripcion corta es obligatoria.")
    private String shortDescription;

    @NotBlank(message = "El pseudonimo es obligatorio.")
    private String nickname;

    @NotBlank(message = "El tipo de descuento es obligatorio.")
    private String discountType;

    @NotNull(message = "El estado es obligatorio.")
    private Boolean activo;

    @NotNull(message = "El valor del descuento es obligatorio.")
    @DecimalMin(value = "0.00", inclusive = true, message = "El valor debe ser mayor o igual a 0.")
    private BigDecimal value;

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
