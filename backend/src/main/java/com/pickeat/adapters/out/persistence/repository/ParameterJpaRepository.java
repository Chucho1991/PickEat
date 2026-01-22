package com.pickeat.adapters.out.persistence.repository;

import com.pickeat.adapters.out.persistence.entity.ParameterJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA para parametros de configuracion.
 */
public interface ParameterJpaRepository extends JpaRepository<ParameterJpaEntity, String> {
}
