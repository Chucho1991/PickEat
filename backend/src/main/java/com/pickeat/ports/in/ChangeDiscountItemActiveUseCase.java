package com.pickeat.ports.in;

import com.pickeat.domain.DiscountItem;
import com.pickeat.domain.DiscountItemId;

/**
 * Caso de uso para cambiar el estado activo de descuentos.
 */
public interface ChangeDiscountItemActiveUseCase {
    DiscountItem changeActive(DiscountItemId id, boolean active);
}
