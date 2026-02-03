package com.pickeat.ports.in;

import java.util.UUID;

/**
 * Caso de uso para eliminar campos de facturacion.
 */
public interface DeleteOrderBillingFieldUseCase {
    /**
     * Elimina un campo de facturacion.
     *
     * @param id identificador del campo.
     */
    void delete(UUID id);
}
