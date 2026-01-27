package com.pickeat.ports.in;

import com.pickeat.domain.Order;
import com.pickeat.domain.OrderId;

/**
 * Caso de uso para cambiar el estado activo de una orden.
 */
public interface ChangeOrderActiveUseCase {
    /**
     * Cambia el estado activo de una orden.
     *
     * @param id identificador de la orden.
     * @param activo nuevo estado.
     * @return orden actualizada.
     */
    Order changeActive(OrderId id, boolean activo);
}
