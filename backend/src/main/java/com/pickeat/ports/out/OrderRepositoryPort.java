package com.pickeat.ports.out;

import com.pickeat.domain.Order;
import com.pickeat.domain.OrderId;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para persistencia de ordenes.
 */
public interface OrderRepositoryPort {
    /**
     * Retorna todas las ordenes registradas.
     *
     * @return listado completo.
     */
    List<Order> findAll();

    Order save(Order order);

    Optional<Order> findById(OrderId id);
}
