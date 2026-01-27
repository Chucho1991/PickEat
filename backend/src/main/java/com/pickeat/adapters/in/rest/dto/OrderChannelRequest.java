package com.pickeat.adapters.in.rest.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Solicitud para crear o actualizar canal de orden.
 */
public class OrderChannelRequest {
    @NotBlank(message = "El nombre es obligatorio.")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
