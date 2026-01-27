package com.pickeat.ports.in;

import com.pickeat.domain.Order;
import com.pickeat.domain.OrderId;

/**
 * Caso de uso para restaurar ordenes eliminadas.
 */
public interface RestoreOrderUseCase {
    /**
     * Restaura una orden eliminada.
     *
     * @param id identificador de la orden.
     * @return orden restaurada.
     */
    Order restore(OrderId id);
}
