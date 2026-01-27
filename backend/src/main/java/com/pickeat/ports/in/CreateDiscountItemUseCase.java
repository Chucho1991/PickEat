package com.pickeat.ports.in;

import com.pickeat.domain.DiscountItem;

/**
 * Caso de uso para crear descuentos.
 */
public interface CreateDiscountItemUseCase {
    DiscountItem create(DiscountItem discountItem);
}
