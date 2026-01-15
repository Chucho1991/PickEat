package com.pickeat.application.usecase;

import com.pickeat.domain.AppModule;
import com.pickeat.domain.Role;
import com.pickeat.domain.RoleModuleSetting;
import com.pickeat.ports.in.UpdateRoleModulesUseCase;
import com.pickeat.ports.out.RoleModuleRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Servicio para actualizar módulos habilitados por rol.
 */
@Service
public class UpdateRoleModulesService implements UpdateRoleModulesUseCase {
    private final RoleModuleRepositoryPort repository;

    /**
     * Crea el servicio de actualización.
     *
     * @param repository repositorio de módulos.
     */
    public UpdateRoleModulesService(RoleModuleRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public void update(Role role, List<RoleModuleSetting> modules) {
        Map<AppModule, RoleModuleSetting> normalized = new EnumMap<>(AppModule.class);
        for (AppModule module : AppModule.values()) {
            normalized.put(module, new RoleModuleSetting(role, module, false));
        }
        for (RoleModuleSetting setting : modules) {
            normalized.put(setting.getModule(), new RoleModuleSetting(role, setting.getModule(), setting.isEnabled()));
        }
        repository.saveAll(role, List.copyOf(normalized.values()));
    }
}
