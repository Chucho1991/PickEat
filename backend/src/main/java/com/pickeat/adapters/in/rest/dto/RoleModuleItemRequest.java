package com.pickeat.adapters.in.rest.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO para actualizar un módulo habilitado por rol.
 */
public class RoleModuleItemRequest {
    @NotBlank(message = "El módulo es obligatorio.")
    private String key;
    private boolean enabled;

    /**
     * Obtiene la llave del módulo.
     *
     * @return llave del módulo.
     */
    public String getKey() {
        return key;
    }

    /**
     * Asigna la llave del módulo.
     *
     * @param key llave del módulo.
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Indica si el módulo está habilitado.
     *
     * @return {@code true} si está habilitado.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Asigna el estado del módulo.
     *
     * @param enabled estado de habilitación.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
