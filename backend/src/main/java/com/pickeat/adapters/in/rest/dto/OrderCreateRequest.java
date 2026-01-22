package com.pickeat.adapters.in.rest.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

/**
 * Solicitud para crear una orden.
 */
public class OrderCreateRequest {
    @NotNull(message = "La mesa es obligatoria.")
    private UUID mesaId;

    @NotEmpty(message = "Debe agregar al menos un item.")
    @Valid
    private List<OrderItemRequest> items;

    public UUID getMesaId() {
        return mesaId;
    }

    public void setMesaId(UUID mesaId) {
        this.mesaId = mesaId;
    }

    public List<OrderItemRequest> getItems() {
        return items;
    }

    public void setItems(List<OrderItemRequest> items) {
        this.items = items;
    }
}
