package com.pickeat.application.usecase;

import com.pickeat.domain.Order;
import com.pickeat.domain.OrderId;
import com.pickeat.domain.OrderStatus;
import com.pickeat.ports.in.RestoreOrderUseCase;
import com.pickeat.ports.out.OrderRepositoryPort;
import org.springframework.stereotype.Service;

/**
 * Caso de uso para restaurar ordenes eliminadas.
 */
@Service
public class RestoreOrderService implements RestoreOrderUseCase {
    private final OrderRepositoryPort repository;

    public RestoreOrderService(OrderRepositoryPort repository) {
        this.repository = repository;
    }

    /**
     * Restaura la orden eliminada.
     *
     * @param id identificador de la orden.
     * @return orden restaurada.
     */
    @Override
    public Order restore(OrderId id) {
        Order order = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("La orden no existe."));
        order.setDeleted(false);
        order.setStatus(OrderStatus.INACTIVA);
        order.setActive(false);
        return repository.save(order);
    }
}
