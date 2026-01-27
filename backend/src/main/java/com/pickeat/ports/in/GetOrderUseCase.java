package com.pickeat.ports.in;

import com.pickeat.domain.Order;
import com.pickeat.domain.OrderId;

import java.util.List;

/**
 * Caso de uso para consultar ordenes.
 */
public interface GetOrderUseCase {
    /**
     * Retorna el listado completo de ordenes.
     *
     * @return ordenes registradas.
     */
    List<Order> getAll();

    /**
     * Obtiene una orden por id.
     *
     * @param id identificador de la orden.
     * @return orden encontrada.
     */
    Order getById(OrderId id);
}
