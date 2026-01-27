package com.pickeat.ports.in;

import com.pickeat.domain.Order;
import com.pickeat.domain.OrderDraft;
import com.pickeat.domain.OrderId;

/**
 * Caso de uso para actualizar ordenes.
 */
public interface UpdateOrderUseCase {
    /**
     * Actualiza una orden con los datos suministrados.
     *
     * @param id identificador de la orden.
     * @param draft datos de la orden.
     * @return orden actualizada.
     */
    Order update(OrderId id, OrderDraft draft);
}
