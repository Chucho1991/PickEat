package com.pickeat.adapters.in.rest.dto;

import jakarta.validation.constraints.NotNull;

/**
 * Solicitud para cambiar estado activo de descuento.
 */
public class DiscountItemActiveRequest {
    @NotNull(message = "El estado es obligatorio.")
    private Boolean activo;

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
