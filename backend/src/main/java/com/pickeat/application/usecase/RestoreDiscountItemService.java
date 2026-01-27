package com.pickeat.application.usecase;

import com.pickeat.domain.DiscountItem;
import com.pickeat.domain.DiscountItemId;
import com.pickeat.ports.in.RestoreDiscountItemUseCase;
import com.pickeat.ports.out.DiscountItemRepositoryPort;
import org.springframework.stereotype.Service;

/**
 * Servicio de aplicación para restaurar descuentos.
 */
@Service
public class RestoreDiscountItemService implements RestoreDiscountItemUseCase {
    private final DiscountItemRepositoryPort repository;

    public RestoreDiscountItemService(DiscountItemRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public DiscountItem restore(DiscountItemId id) {
        DiscountItem item = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Descuento no encontrado."));
        item.setDeleted(false);
        item.setActive(true);
        return repository.save(item);
    }
}
