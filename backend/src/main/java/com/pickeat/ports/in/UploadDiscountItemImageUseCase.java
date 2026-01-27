package com.pickeat.ports.in;

import com.pickeat.domain.DiscountItem;
import com.pickeat.domain.DiscountItemId;
import com.pickeat.domain.DiscountItemImage;

/**
 * Caso de uso para cargar imagen de descuentos.
 */
public interface UploadDiscountItemImageUseCase {
    DiscountItem upload(DiscountItemId id, DiscountItemImage image);
}
