package com.pickeat.adapters.out.persistence;

import com.pickeat.adapters.out.persistence.entity.MenuItemJpaEntity;
import com.pickeat.adapters.out.persistence.repository.MenuItemJpaRepository;
import com.pickeat.domain.DishType;
import com.pickeat.domain.MenuItem;
import com.pickeat.domain.MenuItemId;
import com.pickeat.domain.MenuItemStatus;
import com.pickeat.ports.out.MenuItemRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Adaptador de persistencia para ítems del menú.
 */
@Repository
public class MenuItemPersistenceAdapter implements MenuItemRepositoryPort {
    private final MenuItemJpaRepository repository;

    /**
     * Construye el adaptador con el repositorio requerido.
     *
     * @param repository repositorio JPA.
     */
    public MenuItemPersistenceAdapter(MenuItemJpaRepository repository) {
        this.repository = repository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MenuItem save(MenuItem menuItem) {
        MenuItemJpaEntity entity = toEntity(menuItem);
        MenuItemJpaEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<MenuItem> findById(MenuItemId id) {
        return repository.findById(id.getValue()).map(this::toDomain);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<MenuItem> findByNickname(String nickname) {
        return repository.findByNickname(nickname).map(this::toDomain);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MenuItem> findAll(DishType dishType, MenuItemStatus status, String search) {
        return repository.findAllFiltered(dishType, status, search)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    /**
     * Convierte dominio a entidad JPA.
     *
     * @param menuItem ítem de dominio.
     * @return entidad JPA.
     */
    private MenuItemJpaEntity toEntity(MenuItem menuItem) {
        MenuItemJpaEntity entity = new MenuItemJpaEntity();
        UUID id = menuItem.getId() != null ? menuItem.getId().getValue() : null;
        entity.setId(id);
        entity.setLongDescription(menuItem.getLongDescription());
        entity.setShortDescription(menuItem.getShortDescription());
        entity.setNickname(menuItem.getNickname());
        entity.setDishType(menuItem.getDishType());
        entity.setStatus(menuItem.getStatus());
        entity.setPrice(menuItem.getPrice());
        entity.setImagePath(menuItem.getImagePath());
        entity.setCreatedAt(menuItem.getCreatedAt());
        entity.setUpdatedAt(menuItem.getUpdatedAt());
        return entity;
    }

    /**
     * Convierte entidad JPA a dominio.
     *
     * @param entity entidad JPA.
     * @return ítem de dominio.
     */
    private MenuItem toDomain(MenuItemJpaEntity entity) {
        return new MenuItem(
                new MenuItemId(entity.getId()),
                entity.getLongDescription(),
                entity.getShortDescription(),
                entity.getNickname(),
                entity.getDishType(),
                entity.getStatus(),
                entity.getPrice(),
                entity.getImagePath(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
