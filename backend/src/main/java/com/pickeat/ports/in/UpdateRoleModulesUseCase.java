package com.pickeat.ports.in;

import com.pickeat.domain.Role;
import com.pickeat.domain.RoleModuleSetting;

import java.util.List;

/**
 * Caso de uso para actualizar módulos habilitados por rol.
 */
public interface UpdateRoleModulesUseCase {
    /**
     * Actualiza la configuración de módulos para un rol.
     *
     * @param role     rol a actualizar.
     * @param modules  módulos configurados.
     */
    void update(Role role, List<RoleModuleSetting> modules);
}
