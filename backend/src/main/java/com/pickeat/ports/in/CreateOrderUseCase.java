package com.pickeat.ports.in;

import com.pickeat.domain.Order;
import com.pickeat.domain.OrderDraft;

/**
 * Caso de uso para crear ordenes.
 */
public interface CreateOrderUseCase {
    Order create(OrderDraft draft);
}
