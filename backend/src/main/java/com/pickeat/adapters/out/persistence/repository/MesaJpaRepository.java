package com.pickeat.adapters.out.persistence.repository;

import com.pickeat.adapters.out.persistence.entity.MesaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;
import java.util.List;

public interface MesaJpaRepository extends JpaRepository<MesaJpaEntity, UUID> {
    @Query("""
        SELECT m
        FROM MesaJpaEntity m
        WHERE (:includeDeleted = true OR m.deleted = false)
        ORDER BY m.description ASC
    """)
    List<MesaJpaEntity> findAllFiltered(@Param("includeDeleted") boolean includeDeleted);
}
