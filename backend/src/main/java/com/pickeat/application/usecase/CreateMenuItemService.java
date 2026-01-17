package com.pickeat.application.usecase;

import com.pickeat.domain.MenuItem;
import com.pickeat.ports.in.CreateMenuItemUseCase;
import com.pickeat.ports.out.MenuItemRepositoryPort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Servicio de aplicación para crear ítems de menú.
 */
@Service
public class CreateMenuItemService implements CreateMenuItemUseCase {
    private final MenuItemRepositoryPort repository;

    /**
     * Crea el servicio con el repositorio requerido.
     *
     * @param repository repositorio de menú.
     */
    public CreateMenuItemService(MenuItemRepositoryPort repository) {
        this.repository = repository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MenuItem create(MenuItem menuItem) {
        validatePrice(menuItem.getPrice());
        repository.findByNickname(menuItem.getNickname()).ifPresent(existing -> {
            throw new IllegalArgumentException("El pseudónimo ya está en uso.");
        });
        return repository.save(menuItem);
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
