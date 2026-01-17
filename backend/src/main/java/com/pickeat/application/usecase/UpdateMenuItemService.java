package com.pickeat.application.usecase;

import com.pickeat.domain.MenuItem;
import com.pickeat.domain.MenuItemId;
import com.pickeat.ports.in.UpdateMenuItemUseCase;
import com.pickeat.ports.out.MenuItemRepositoryPort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Servicio de aplicación para actualizar ítems del menú.
 */
@Service
public class UpdateMenuItemService implements UpdateMenuItemUseCase {
    private final MenuItemRepositoryPort repository;

    /**
     * Construye el servicio con el repositorio requerido.
     *
     * @param repository repositorio de menú.
     */
    public UpdateMenuItemService(MenuItemRepositoryPort repository) {
        this.repository = repository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MenuItem update(MenuItemId id, MenuItem menuItem) {
        MenuItem existing = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ítem de menú no encontrado."));
        validatePrice(menuItem.getPrice());
        repository.findByNickname(menuItem.getNickname()).ifPresent(found -> {
            if (!found.getId().equals(id)) {
                throw new IllegalArgumentException("El pseudónimo ya está en uso.");
            }
        });
        existing.setLongDescription(menuItem.getLongDescription());
        existing.setShortDescription(menuItem.getShortDescription());
        existing.setNickname(menuItem.getNickname());
        existing.setDishType(menuItem.getDishType());
        existing.setStatus(menuItem.getStatus());
        existing.setPrice(menuItem.getPrice());
        existing.setUpdatedAt(Instant.now());
        return repository.save(existing);
    }

    /**
     * Valida que el precio sea mayor o igual a cero.
     *
     * @param price precio a validar.
     */
    private void validatePrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio debe ser mayor o igual a 0.");
        }
    }
}
