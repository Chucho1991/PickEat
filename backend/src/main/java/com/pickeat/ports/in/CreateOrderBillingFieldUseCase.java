package com.pickeat.ports.in;

import com.pickeat.domain.OrderBillingField;

/**
 * Caso de uso para crear campos de facturacion.
 */
public interface CreateOrderBillingFieldUseCase {
    /**
     * Crea un campo de facturacion.
     *
     * @param field campo base.
     * @return campo creado.
     */
    OrderBillingField create(OrderBillingField field);
}
