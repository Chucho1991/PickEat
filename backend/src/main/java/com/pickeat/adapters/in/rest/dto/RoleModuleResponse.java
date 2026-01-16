package com.pickeat.adapters.in.rest.dto;

/**
 * DTO para representar un módulo habilitado por rol.
 */
public class RoleModuleResponse {
    private String key;
    private String label;
    private boolean enabled;

    public RoleModuleResponse(String key, String label, boolean enabled) {
        this.key = key;
        this.label = label;
        this.enabled = enabled;
    }

    /**
     * Obtiene la llave del módulo.
     *
     * @return llave del módulo.
     */
    public String getKey() {
        return key;
    }

    /**
     * Obtiene la etiqueta del módulo.
     *
     * @return etiqueta del módulo.
     */
    public String getLabel() {
        return label;
    }

    /**
     * Indica si el módulo está habilitado.
     *
     * @return {@code true} si está habilitado.
     */
    public boolean isEnabled() {
        return enabled;
    }
}
