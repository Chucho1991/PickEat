package com.pickeat.application.usecase;

import com.pickeat.domain.Order;
import com.pickeat.domain.OrderId;
import com.pickeat.ports.in.GetOrderUseCase;
import com.pickeat.ports.out.OrderRepositoryPort;
import org.springframework.stereotype.Service;

/**
 * Servicio de aplicacion para consultar ordenes.
 */
@Service
public class GetOrderService implements GetOrderUseCase {
    private final OrderRepositoryPort orderRepository;

    public GetOrderService(OrderRepositoryPort orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order getById(OrderId id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("La orden no existe."));
    }
}
