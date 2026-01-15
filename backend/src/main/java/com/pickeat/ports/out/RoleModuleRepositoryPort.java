package com.pickeat.ports.out;

import com.pickeat.domain.Role;
import com.pickeat.domain.RoleModuleSetting;

import java.util.List;

/**
 * Puerto para la persistencia de módulos por rol.
 */
public interface RoleModuleRepositoryPort {
    /**
     * Obtiene los módulos configurados para un rol.
     *
     * @param role rol a consultar.
     * @return listado de configuraciones.
     */
    List<RoleModuleSetting> findByRole(Role role);

    /**
     * Persiste la configuración de módulos para un rol, reemplazando el estado anterior.
     *
     * @param role     rol a actualizar.
     * @param modules  configuraciones de módulos.
     */
    void saveAll(Role role, List<RoleModuleSetting> modules);
}
