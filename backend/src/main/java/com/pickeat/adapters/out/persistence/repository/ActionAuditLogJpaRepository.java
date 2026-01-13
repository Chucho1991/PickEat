package com.pickeat.adapters.out.persistence.repository;

import com.pickeat.adapters.out.persistence.entity.ActionAuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActionAuditLogJpaRepository extends JpaRepository<ActionAuditLogEntity, Long> {
}
