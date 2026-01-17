package com.pickeat.adapters.in.rest.dto;

import com.pickeat.domain.MenuItemStatus;
import jakarta.validation.constraints.NotNull;

/**
 * Solicitud para cambiar el estado de un ítem del menú.
 */
public class MenuItemStatusRequest {
    @NotNull(message = "El estado es obligatorio.")
    private MenuItemStatus status;

    /**
     * Obtiene el estado.
     *
     * @return estado.
     */
    public MenuItemStatus getStatus() {
        return status;
    }

    /**
     * Actualiza el estado.
     *
     * @param status estado.
     */
    public void setStatus(MenuItemStatus status) {
        this.status = status;
    }
}
