package com.pickeat.ports.in;

import com.pickeat.domain.DiscountItemId;

/**
 * Caso de uso para eliminar descuentos.
 */
public interface DeleteDiscountItemUseCase {
    void delete(DiscountItemId id);
}
