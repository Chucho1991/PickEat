package com.pickeat.adapters.out.persistence.repository;

import com.pickeat.adapters.out.persistence.entity.MenuItemJpaEntity;
import com.pickeat.domain.DishType;
import com.pickeat.domain.MenuItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio JPA para ítems del menú.
 */
public interface MenuItemJpaRepository extends JpaRepository<MenuItemJpaEntity, UUID> {

    /**
     * Busca por pseudónimo.
     *
     * @param nickname pseudónimo.
     * @return ítem opcional.
     */
    Optional<MenuItemJpaEntity> findByNickname(String nickname);

    /**
     * Lista ítems aplicando filtros.
     *
     * @param dishType tipo de plato.
     * @param status estado.
     * @param search término de búsqueda.
     * @return listado de ítems.
     */
    @Query("""
        SELECT m
        FROM MenuItemJpaEntity m
        WHERE (:dishType IS NULL OR m.dishType = :dishType)
          AND (:status IS NULL OR m.status = :status)
          AND (
                :search IS NULL OR :search = '' OR
                LOWER(m.longDescription)  LIKE LOWER(CONCAT('%', :search, '%')) OR
                LOWER(m.shortDescription) LIKE LOWER(CONCAT('%', :search, '%')) OR
                LOWER(m.nickname)         LIKE LOWER(CONCAT('%', :search, '%'))
          )
        ORDER BY m.createdAt DESC
    """)
    List<MenuItemJpaEntity> findAllFiltered(@Param("dishType") DishType dishType,
                                            @Param("status") MenuItemStatus status,
                                            @Param("search") String search);
}
