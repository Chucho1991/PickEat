package com.pickeat.adapters.out.persistence.repository;

import com.pickeat.adapters.out.persistence.entity.CouponJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CouponJpaRepository extends JpaRepository<CouponJpaEntity, UUID> {
    Optional<CouponJpaEntity> findByCode(String code);

    List<CouponJpaEntity> findAllByGeneratedOrderId(UUID orderId);

    List<CouponJpaEntity> findAllByRedeemedOrderId(UUID orderId);
}
