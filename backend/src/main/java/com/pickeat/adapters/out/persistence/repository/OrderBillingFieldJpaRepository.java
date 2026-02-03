package com.pickeat.adapters.out.persistence.repository;

import com.pickeat.adapters.out.persistence.entity.OrderBillingFieldJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderBillingFieldJpaRepository extends JpaRepository<OrderBillingFieldJpaEntity, UUID> {
    List<OrderBillingFieldJpaEntity> findAllByDeletedFalseOrderBySortOrderAsc();
}
