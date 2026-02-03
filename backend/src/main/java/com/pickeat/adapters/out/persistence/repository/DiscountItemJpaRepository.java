package com.pickeat.adapters.out.persistence.repository;

import com.pickeat.adapters.out.persistence.entity.DiscountItemJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio JPA para descuentos.
 */
public interface DiscountItemJpaRepository extends JpaRepository<DiscountItemJpaEntity, UUID> {
    Optional<DiscountItemJpaEntity> findByNickname(String nickname);

    @Query("""
        SELECT d
        FROM DiscountItemJpaEntity d
        WHERE (:includeDeleted = true OR d.deleted = false)
          AND (:activo IS NULL OR d.active = :activo)
          AND (
                :search IS NULL OR :search = '' OR
                LOWER(d.longDescription)  LIKE LOWER(CONCAT('%', :search, '%')) OR
                LOWER(d.shortDescription) LIKE LOWER(CONCAT('%', :search, '%')) OR
                LOWER(d.nickname)         LIKE LOWER(CONCAT('%', :search, '%'))
          )
        ORDER BY d.createdAt DESC
    """)
    List<DiscountItemJpaEntity> findAllFiltered(@Param("activo") Boolean activo,
                                                @Param("search") String search,
                                                @Param("includeDeleted") boolean includeDeleted);

    @Query("""
        SELECT d
        FROM DiscountItemJpaEntity d
        WHERE d.generatesCoupon = true AND d.couponActive = true AND d.deleted = false AND d.active = true
        """)
    List<DiscountItemJpaEntity> findCouponGenerators();

    @Query("""
        SELECT d
        FROM DiscountItemJpaEntity d
        WHERE d.applyScope = 'ITEM' AND d.autoApply = true AND d.deleted = false AND d.active = true
        """)
    List<DiscountItemJpaEntity> findAutoApplyItemDiscounts();
}
