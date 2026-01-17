package com.pickeat.adapters.in.rest.dto;

import jakarta.validation.constraints.NotNull;

/**
 * Solicitud para actualizar el estado de una mesa.
 */
public class MesaActiveRequest {
    @NotNull
    private Boolean activo;

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
