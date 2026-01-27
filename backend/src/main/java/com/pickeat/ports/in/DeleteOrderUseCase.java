package com.pickeat.ports.in;

import com.pickeat.domain.OrderId;

/**
 * Caso de uso para eliminar ordenes de forma logica.
 */
public interface DeleteOrderUseCase {
    /**
     * Elimina una orden de forma logica.
     *
     * @param id identificador de la orden.
     */
    void delete(OrderId id);
}
