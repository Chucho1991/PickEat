package com.pickeat.application.usecase;

import com.pickeat.domain.AppModule;
import com.pickeat.domain.Role;
import com.pickeat.domain.RoleModuleSetting;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

/**
 * Define las configuraciones por defecto de módulos por rol.
 */
@Component
public class RoleModuleCatalog {
    /**
     * Obtiene la configuración por defecto para un rol.
     *
     * @param role rol a consultar.
     * @return listado de módulos con su estado por defecto.
     */
    public List<RoleModuleSetting> defaultsFor(Role role) {
        return Stream.of(AppModule.values())
                .map(module -> new RoleModuleSetting(role, module, isEnabledByDefault(role, module)))
                .toList();
    }

    private boolean isEnabledByDefault(Role role, AppModule module) {
        return switch (role) {
            case SUPERADMINISTRADOR, ADMINISTRADOR -> true;
            case MESERO -> switch (module) {
                case DASHBOARD, PROFILE, MESAS, ORDENES -> true;
                default -> false;
            };
            case DESPACHADOR -> switch (module) {
                case DASHBOARD, PROFILE, ORDENES, DESPACHADOR -> true;
                default -> false;
            };
        };
    }
}
