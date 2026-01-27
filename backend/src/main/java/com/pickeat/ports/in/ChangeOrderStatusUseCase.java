package com.pickeat.ports.in;

import com.pickeat.domain.Order;
import com.pickeat.domain.OrderId;
import com.pickeat.domain.OrderStatus;

/**
 * Caso de uso para cambiar el estado de una orden.
 */
public interface ChangeOrderStatusUseCase {
    Order changeStatus(OrderId id, OrderStatus status);
}
