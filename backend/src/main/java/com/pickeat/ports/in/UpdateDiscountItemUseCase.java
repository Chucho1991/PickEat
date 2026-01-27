package com.pickeat.ports.in;

import com.pickeat.domain.DiscountItem;
import com.pickeat.domain.DiscountItemId;

/**
 * Caso de uso para actualizar descuentos.
 */
public interface UpdateDiscountItemUseCase {
    DiscountItem update(DiscountItemId id, DiscountItem discountItem);
}
