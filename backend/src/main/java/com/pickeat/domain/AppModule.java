package com.pickeat.domain;

import java.util.Arrays;

/**
 * Enumeración de módulos disponibles para configurar accesos por rol.
 */
public enum AppModule {
    DASHBOARD("dashboard", "Dashboard"),
    USERS("users", "Usuarios"),
    PROFILE("profile", "Perfil"),
    MESAS("mesas", "Mesas"),
    MENU("menu", "Menu"),
    ORDENES("ordenes", "Ordenes"),
    DESPACHADOR("despachador", "Despachador"),
    ROLE_MODULES("role-modules", "Accesos");

    private final String key;
    private final String label;

    AppModule(String key, String label) {
        this.key = key;
        this.label = label;
    }

    /**
     * Obtiene la llave del módulo.
     *
     * @return llave persistible del módulo.
     */
    public String getKey() {
        return key;
    }

    /**
     * Obtiene el nombre legible del módulo.
     *
     * @return etiqueta del módulo.
     */
    public String getLabel() {
        return label;
    }

    /**
     * Obtiene el módulo a partir de la llave persistida.
     *
     * @param key llave del módulo.
     * @return módulo asociado.
     */
    public static AppModule fromKey(String key) {
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("El módulo es obligatorio.");
        }
        return Arrays.stream(values())
                .filter(module -> module.key.equalsIgnoreCase(key.trim()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Módulo no reconocido: " + key));
    }
}
