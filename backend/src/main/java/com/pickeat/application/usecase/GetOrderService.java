package com.pickeat.application.usecase;

import com.pickeat.domain.Order;
import com.pickeat.domain.OrderId;
import com.pickeat.ports.in.GetOrderUseCase;
import com.pickeat.ports.out.OrderRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * Servicio de aplicacion para consultar ordenes.
 */
@Service
public class GetOrderService implements GetOrderUseCase {
    private final OrderRepositoryPort orderRepository;

    public GetOrderService(OrderRepositoryPort orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     * Retorna el listado de ordenes ordenado por fecha de creacion descendente.
     *
     * @return ordenes registradas.
     */
    @Override
    public List<Order> getAll() {
        return orderRepository.findAll().stream()
                .sorted(Comparator.comparing(Order::getCreatedAt).reversed())
                .toList();
    }

    /**
     * Retorna una orden por id.
     *
     * @param id identificador de la orden.
     * @return orden encontrada.
     */
    @Override
    public Order getById(OrderId id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("La orden no existe."));
    }
}
