package com.pickeat.adapters.out.persistence.repository;

import com.pickeat.adapters.out.persistence.entity.ApiAuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiAuditLogJpaRepository extends JpaRepository<ApiAuditLogEntity, Long> {
}
