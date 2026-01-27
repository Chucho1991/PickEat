package com.pickeat.adapters.in.rest.dto;

import com.pickeat.domain.DishType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Solicitud para crear o actualizar un item del menu.
 */
public class MenuItemRequest {
    @NotBlank(message = "La descripcion larga es obligatoria.")
    private String longDescription;

    @NotBlank(message = "La descripcion corta es obligatoria.")
    private String shortDescription;

    @NotBlank(message = "El pseudonimo es obligatorio.")
    private String nickname;

    @NotNull(message = "El tipo de plato es obligatorio.")
    private DishType dishType;

    @NotNull(message = "El estado es obligatorio.")
    private Boolean activo;

    @NotNull(message = "Debes indicar si aplica impuesto.")
    private Boolean aplicaImpuesto;

    @NotNull(message = "El precio es obligatorio.")
    @DecimalMin(value = "0.00", inclusive = true, message = "El precio debe ser mayor o igual a 0.")
    private BigDecimal price;

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

    public DishType getDishType() {
        return dishType;
    }

    public void setDishType(DishType dishType) {
        this.dishType = dishType;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Boolean getAplicaImpuesto() {
        return aplicaImpuesto;
    }

    public void setAplicaImpuesto(Boolean aplicaImpuesto) {
        this.aplicaImpuesto = aplicaImpuesto;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
