package com.pickeat.adapters.out.persistence.repository;

import com.pickeat.adapters.out.persistence.entity.OrderChannelJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio JPA para canales de orden.
 */
public interface OrderChannelJpaRepository extends JpaRepository<OrderChannelJpaEntity, UUID> {
    Optional<OrderChannelJpaEntity> findByName(String name);

    @Query("SELECT c FROM OrderChannelJpaEntity c WHERE c.isDefault = true")
    Optional<OrderChannelJpaEntity> findDefault();

    List<OrderChannelJpaEntity> findAllByDeletedFalse();
}
