package com.pickeat.adapters.in.rest.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/**
 * DTO para actualizar la configuración de módulos por rol.
 */
public class RoleModuleUpdateRequest {
    @NotEmpty(message = "Debe enviar al menos un módulo.")
    @Valid
    private List<RoleModuleItemRequest> modules;

    /**
     * Obtiene los módulos a actualizar.
     *
     * @return listado de módulos.
     */
    public List<RoleModuleItemRequest> getModules() {
        return modules;
    }

    /**
     * Asigna los módulos a actualizar.
     *
     * @param modules listado de módulos.
     */
    public void setModules(List<RoleModuleItemRequest> modules) {
        this.modules = modules;
    }
}
