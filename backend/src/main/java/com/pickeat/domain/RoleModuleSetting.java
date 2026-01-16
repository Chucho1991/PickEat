package com.pickeat.domain;

/**
 * Representa la habilitación de un módulo para un rol.
 */
public class RoleModuleSetting {
    private final Role role;
    private final AppModule module;
    private final boolean enabled;

    /**
     * Crea una configuración de módulo para un rol.
     *
     * @param role    rol asociado.
     * @param module  módulo configurado.
     * @param enabled indica si el módulo está habilitado.
     */
    public RoleModuleSetting(Role role, AppModule module, boolean enabled) {
        this.role = role;
        this.module = module;
        this.enabled = enabled;
    }

    /**
     * Obtiene el rol asociado.
     *
     * @return rol configurado.
     */
    public Role getRole() {
        return role;
    }

    /**
     * Obtiene el módulo configurado.
     *
     * @return módulo configurado.
     */
    public AppModule getModule() {
        return module;
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
