package com.pickeat.adapters.out.persistence;

import com.pickeat.adapters.out.persistence.entity.MenuItemJpaEntity;
import com.pickeat.adapters.out.persistence.repository.MenuItemJpaRepository;
import com.pickeat.domain.DishType;
import com.pickeat.domain.MenuItem;
import com.pickeat.domain.MenuItemId;
import com.pickeat.ports.out.MenuItemRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Adaptador de persistencia para items del menu.
 */
@Repository
public class MenuItemPersistenceAdapter implements MenuItemRepositoryPort {
    private final MenuItemJpaRepository repository;

    public MenuItemPersistenceAdapter(MenuItemJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public MenuItem save(MenuItem menuItem) {
        MenuItemJpaEntity entity = toEntity(menuItem);
        MenuItemJpaEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<MenuItem> findById(MenuItemId id) {
        return repository.findById(id.getValue()).map(this::toDomain);
    }

    @Override
    public Optional<MenuItem> findByNickname(String nickname) {
        return repository.findByNickname(nickname).map(this::toDomain);
    }

    @Override
    public List<MenuItem> findAll(DishType dishType, Boolean activo, String search, boolean includeDeleted) {
        return repository.findAllFiltered(dishType, activo, search, includeDeleted)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    private MenuItemJpaEntity toEntity(MenuItem menuItem) {
        MenuItemJpaEntity entity = new MenuItemJpaEntity();
        UUID id = menuItem.getId() != null ? menuItem.getId().getValue() : null;
        entity.setId(id);
        entity.setLongDescription(menuItem.getLongDescription());
        entity.setShortDescription(menuItem.getShortDescription());
        entity.setNickname(menuItem.getNickname());
        entity.setDishType(menuItem.getDishType());
        entity.setActive(menuItem.isActive());
        entity.setDeleted(menuItem.isDeleted());
        entity.setApplyTax(menuItem.isApplyTax());
        entity.setPrice(menuItem.getPrice());
        entity.setImagePath(menuItem.getImagePath());
        entity.setCreatedAt(menuItem.getCreatedAt());
        entity.setUpdatedAt(menuItem.getUpdatedAt());
        return entity;
    }

    private MenuItem toDomain(MenuItemJpaEntity entity) {
        return new MenuItem(
                new MenuItemId(entity.getId()),
                entity.getLongDescription(),
                entity.getShortDescription(),
                entity.getNickname(),
                entity.getDishType(),
                entity.isActive(),
                entity.isDeleted(),
                entity.isApplyTax(),
                entity.getPrice(),
                entity.getImagePath(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
