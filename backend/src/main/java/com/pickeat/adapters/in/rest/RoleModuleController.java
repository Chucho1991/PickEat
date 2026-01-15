package com.pickeat.adapters.in.rest;

import com.pickeat.adapters.in.rest.dto.RoleModuleItemRequest;
import com.pickeat.adapters.in.rest.dto.RoleModuleResponse;
import com.pickeat.adapters.in.rest.dto.RoleModuleUpdateRequest;
import com.pickeat.config.SecurityUtils;
import com.pickeat.domain.AppModule;
import com.pickeat.domain.Role;
import com.pickeat.domain.RoleModuleSetting;
import com.pickeat.ports.in.ListRoleModulesUseCase;
import com.pickeat.ports.in.UpdateRoleModulesUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador REST para configurar módulos habilitados por rol.
 */
@RestController
@RequestMapping("/role-modules")
public class RoleModuleController {
    private final ListRoleModulesUseCase listRoleModulesUseCase;
    private final UpdateRoleModulesUseCase updateRoleModulesUseCase;

    /**
     * Crea el controlador con los casos de uso requeridos.
     *
     * @param listRoleModulesUseCase   caso de uso de consulta.
     * @param updateRoleModulesUseCase caso de uso de actualización.
     */
    public RoleModuleController(ListRoleModulesUseCase listRoleModulesUseCase,
                                UpdateRoleModulesUseCase updateRoleModulesUseCase) {
        this.listRoleModulesUseCase = listRoleModulesUseCase;
        this.updateRoleModulesUseCase = updateRoleModulesUseCase;
    }

    /**
     * Obtiene los módulos habilitados para el rol autenticado.
     *
     * @return listado de módulos habilitados.
     */
    @GetMapping("/me")
    public ResponseEntity<List<RoleModuleResponse>> listForCurrentRole() {
        Role role = Role.from(SecurityUtils.currentRole());
        return ResponseEntity.ok(toResponse(listRoleModulesUseCase.list(role)));
    }

    /**
     * Obtiene los módulos habilitados para un rol específico.
     *
     * @param role rol a consultar.
     * @return listado de módulos.
     */
    @GetMapping("/{role}")
    @PreAuthorize("hasAnyRole('SUPERADMINISTRADOR','ADMINISTRADOR')")
    public ResponseEntity<List<RoleModuleResponse>> listByRole(@PathVariable String role) {
        return ResponseEntity.ok(toResponse(listRoleModulesUseCase.list(Role.from(role))));
    }

    /**
     * Actualiza la configuración de módulos para un rol.
     *
     * @param role    rol a actualizar.
     * @param request configuración enviada.
     * @return respuesta vacía.
     */
    @PutMapping("/{role}")
    @PreAuthorize("hasAnyRole('SUPERADMINISTRADOR','ADMINISTRADOR')")
    public ResponseEntity<Void> update(@PathVariable String role,
                                       @Valid @RequestBody RoleModuleUpdateRequest request) {
        Role parsedRole = Role.from(role);
        List<RoleModuleSetting> settings = request.getModules().stream()
                .map(item -> toSetting(parsedRole, item))
                .toList();
        updateRoleModulesUseCase.update(parsedRole, settings);
        return ResponseEntity.noContent().build();
    }

    private List<RoleModuleResponse> toResponse(List<RoleModuleSetting> settings) {
        return settings.stream()
                .map(setting -> new RoleModuleResponse(
                        setting.getModule().getKey(),
                        setting.getModule().getLabel(),
                        setting.isEnabled()
                ))
                .toList();
    }

    private RoleModuleSetting toSetting(Role role, RoleModuleItemRequest item) {
        AppModule module = AppModule.fromKey(item.getKey());
        return new RoleModuleSetting(role, module, item.isEnabled());
    }
}
