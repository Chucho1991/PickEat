package com.pickeat.application.usecase;

import com.pickeat.domain.AppModule;
import com.pickeat.domain.Role;
import com.pickeat.domain.RoleModuleSetting;
import com.pickeat.ports.in.ListRoleModulesUseCase;
import com.pickeat.ports.out.RoleModuleRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Servicio para listar módulos habilitados por rol.
 */
@Service
public class ListRoleModulesService implements ListRoleModulesUseCase {
    private final RoleModuleRepositoryPort repository;
    private final RoleModuleCatalog catalog;

    /**
     * Crea el servicio de consulta de módulos por rol.
     *
     * @param repository repositorio de módulos.
     * @param catalog    catálogo con defaults.
     */
    public ListRoleModulesService(RoleModuleRepositoryPort repository, RoleModuleCatalog catalog) {
        this.repository = repository;
        this.catalog = catalog;
    }

    @Override
    public List<RoleModuleSetting> list(Role role) {
        List<RoleModuleSetting> stored = repository.findByRole(role);
        List<RoleModuleSetting> defaults = catalog.defaultsFor(role);
        if (stored.isEmpty()) {
            repository.saveAll(role, defaults);
            return defaults;
        }
        Map<AppModule, RoleModuleSetting> merged = new EnumMap<>(AppModule.class);
        for (RoleModuleSetting setting : stored) {
            merged.put(setting.getModule(), setting);
        }
        boolean hasChanges = false;
        for (RoleModuleSetting setting : defaults) {
            if (!merged.containsKey(setting.getModule())) {
                merged.put(setting.getModule(), setting);
                hasChanges = true;
            }
        }
        if (hasChanges) {
            repository.saveAll(role, merged.values().stream().toList());
        }
        return Arrays.stream(AppModule.values())
                .map(merged::get)
                .filter(Objects::nonNull)
                .toList();
    }
}
