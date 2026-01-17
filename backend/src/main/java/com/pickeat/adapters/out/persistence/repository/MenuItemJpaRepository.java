package com.pickeat.adapters.out.persistence.repository;

import com.pickeat.adapters.out.persistence.entity.MenuItemJpaEntity;
import com.pickeat.domain.DishType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio JPA para items del menu.
 */
public interface MenuItemJpaRepository extends JpaRepository<MenuItemJpaEntity, UUID> {

    Optional<MenuItemJpaEntity> findByNickname(String nickname);

    @Query("""
        SELECT m
        FROM MenuItemJpaEntity m
        WHERE (:includeDeleted = true OR m.deleted = false)
          AND (:dishType IS NULL OR m.dishType = :dishType)
          AND (:activo IS NULL OR m.active = :activo)
          AND (
                :search IS NULL OR :search = '' OR
                LOWER(m.longDescription)  LIKE LOWER(CONCAT('%', :search, '%')) OR
                LOWER(m.shortDescription) LIKE LOWER(CONCAT('%', :search, '%')) OR
                LOWER(m.nickname)         LIKE LOWER(CONCAT('%', :search, '%'))
          )
        ORDER BY m.createdAt DESC
    """)
    List<MenuItemJpaEntity> findAllFiltered(@Param("dishType") DishType dishType,
                                            @Param("activo") Boolean activo,
                                            @Param("search") String search,
                                            @Param("includeDeleted") boolean includeDeleted);
}
