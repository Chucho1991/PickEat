package com.pickeat.adapters.out.persistence;

import com.pickeat.adapters.out.persistence.entity.RoleModuleEntity;
import com.pickeat.adapters.out.persistence.entity.RoleModuleId;
import com.pickeat.adapters.out.persistence.repository.RoleModuleJpaRepository;
import com.pickeat.domain.AppModule;
import com.pickeat.domain.Role;
import com.pickeat.domain.RoleModuleSetting;
import com.pickeat.ports.out.RoleModuleRepositoryPort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Adaptador de persistencia para módulos habilitados por rol.
 */
@Repository
public class RoleModulePersistenceAdapter implements RoleModuleRepositoryPort {
    private final RoleModuleJpaRepository repository;

    /**
     * Crea el adaptador con el repositorio JPA.
     *
     * @param repository repositorio JPA.
     */
    public RoleModulePersistenceAdapter(RoleModuleJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<RoleModuleSetting> findByRole(Role role) {
        return repository.findByIdRol(role).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    @Transactional
    public void saveAll(Role role, List<RoleModuleSetting> modules) {
        repository.deleteByIdRol(role);
        List<RoleModuleEntity> entities = modules.stream()
                .map(this::toEntity)
                .toList();
        repository.saveAll(entities);
    }

    private RoleModuleSetting toDomain(RoleModuleEntity entity) {
        AppModule module = AppModule.fromKey(entity.getId().getModuleKey());
        return new RoleModuleSetting(entity.getId().getRol(), module, entity.isEnabled());
    }

    private RoleModuleEntity toEntity(RoleModuleSetting setting) {
        RoleModuleId id = new RoleModuleId(setting.getRole(), setting.getModule().getKey());
        return new RoleModuleEntity(id, setting.isEnabled());
    }
}
