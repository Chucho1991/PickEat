package com.pickeat.adapters.out.persistence.repository;

import com.pickeat.adapters.out.persistence.entity.RoleModuleEntity;
import com.pickeat.adapters.out.persistence.entity.RoleModuleId;
import com.pickeat.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio JPA para módulos habilitados por rol.
 */
public interface RoleModuleJpaRepository extends JpaRepository<RoleModuleEntity, RoleModuleId> {
    /**
     * Obtiene configuraciones por rol.
     *
     * @param rol rol a consultar.
     * @return listado de configuraciones.
     */
    List<RoleModuleEntity> findByIdRol(Role rol);

    /**
     * Elimina configuraciones de un rol.
     *
     * @param rol rol a limpiar.
     */
    void deleteByIdRol(Role rol);
}
