package com.pickeat.ports.in;

import com.pickeat.domain.Order;
import com.pickeat.domain.OrderId;

/**
 * Caso de uso para consultar ordenes.
 */
public interface GetOrderUseCase {
    Order getById(OrderId id);
}
