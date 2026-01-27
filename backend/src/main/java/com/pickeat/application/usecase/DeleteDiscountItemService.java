package com.pickeat.application.usecase;

import com.pickeat.domain.DiscountItemId;
import com.pickeat.ports.in.DeleteDiscountItemUseCase;
import com.pickeat.ports.out.DiscountItemRepositoryPort;
import org.springframework.stereotype.Service;

/**
 * Servicio de aplicación para eliminar descuentos.
 */
@Service
public class DeleteDiscountItemService implements DeleteDiscountItemUseCase {
    private final DiscountItemRepositoryPort repository;

    public DeleteDiscountItemService(DiscountItemRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public void delete(DiscountItemId id) {
        repository.findById(id).ifPresent(item -> {
            item.setDeleted(true);
            item.setActive(false);
            repository.save(item);
        });
    }
}
