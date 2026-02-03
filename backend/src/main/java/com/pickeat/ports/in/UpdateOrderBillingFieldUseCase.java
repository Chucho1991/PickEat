package com.pickeat.ports.in;

import com.pickeat.domain.OrderBillingField;

import java.util.UUID;

/**
 * Caso de uso para actualizar campos de facturacion.
 */
public interface UpdateOrderBillingFieldUseCase {
    /**
     * Actualiza un campo de facturacion.
     *
     * @param id identificador del campo.
     * @param label etiqueta del campo.
     * @param sortOrder orden de visualizacion.
     * @param active estado activo.
     * @return campo actualizado.
     */
    OrderBillingField update(UUID id, String label, Integer sortOrder, Boolean active);
}
