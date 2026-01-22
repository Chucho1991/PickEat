package com.pickeat.adapters.out.persistence.repository;

import com.pickeat.adapters.out.persistence.entity.OrderJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Repositorio JPA para ordenes.
 */
public interface OrderJpaRepository extends JpaRepository<OrderJpaEntity, UUID> {
}
