package com.pickeat.ports.in;

import com.pickeat.domain.Role;
import com.pickeat.domain.RoleModuleSetting;

import java.util.List;

/**
 * Caso de uso para listar los módulos configurados por rol.
 */
public interface ListRoleModulesUseCase {
    /**
     * Obtiene los módulos configurados para el rol solicitado.
     *
     * @param role rol a consultar.
     * @return listado de configuraciones.
     */
    List<RoleModuleSetting> list(Role role);
}
