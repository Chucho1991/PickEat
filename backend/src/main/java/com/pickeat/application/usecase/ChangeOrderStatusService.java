package com.pickeat.application.usecase;

import com.pickeat.domain.Mesa;
import com.pickeat.domain.Order;
import com.pickeat.domain.OrderId;
import com.pickeat.domain.OrderStatus;
import com.pickeat.ports.in.ChangeOrderStatusUseCase;
import com.pickeat.ports.out.MesaRepositoryPort;
import com.pickeat.ports.out.OrderRepositoryPort;
import org.springframework.stereotype.Service;

/**
 * Caso de uso para cambiar el estado de una orden.
 */
@Service
public class ChangeOrderStatusService implements ChangeOrderStatusUseCase {
    private final OrderRepositoryPort orderRepository;
    private final MesaRepositoryPort mesaRepository;

    public ChangeOrderStatusService(OrderRepositoryPort orderRepository, MesaRepositoryPort mesaRepository) {
        this.orderRepository = orderRepository;
        this.mesaRepository = mesaRepository;
    }

    /**
     * Cambia el estado de la orden y ajusta la ocupacion de la mesa.
     *
     * @param id identificador de la orden.
     * @param status nuevo estado.
     * @return orden actualizada.
     */
    @Override
    public Order changeStatus(OrderId id, OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("La orden no existe."));
        if (order.isDeleted()) {
            throw new IllegalArgumentException("La orden esta eliminada.");
        }
        if (status == OrderStatus.ELIMINADA) {
            throw new IllegalArgumentException("Para eliminar use la opcion de eliminar.");
        }

        OrderStatus currentStatus = order.getStatus() != null ? order.getStatus() : OrderStatus.CREADA;
        Mesa mesa = mesaRepository.findById(order.getMesaId())
                .orElseThrow(() -> new IllegalArgumentException("La mesa no existe."));
        if (!mesa.isActive() || mesa.isDeleted()) {
            throw new IllegalArgumentException("La mesa no esta disponible.");
        }

        if (shouldOccupy(status)) {
            if (mesa.isOccupied() && !shouldOccupy(currentStatus)) {
                throw new IllegalArgumentException("La mesa ya esta ocupada.");
            }
            if (!mesa.isOccupied()) {
                mesa.setOccupied(true);
                mesaRepository.save(mesa);
            }
        } else if (shouldRelease(status) && mesa.isOccupied()) {
            mesa.setOccupied(false);
            mesaRepository.save(mesa);
        }

        order.setStatus(status);
        order.setActive(isActiveStatus(status));
        return orderRepository.save(order);
    }

    /**
     * Determina si el estado implica una orden activa.
     *
     * @param status estado a evaluar.
     * @return true si la orden esta activa.
     */
    private boolean isActiveStatus(OrderStatus status) {
        return status == OrderStatus.CREADA
                || status == OrderStatus.PREPARANDOSE
                || status == OrderStatus.DESPACHADA
                || status == OrderStatus.PAGADA;
    }

    /**
     * Determina si el estado implica una mesa ocupada.
     *
     * @param status estado a evaluar.
     * @return true si debe ocupar mesa.
     */
    private boolean shouldOccupy(OrderStatus status) {
        return status == OrderStatus.CREADA
                || status == OrderStatus.PREPARANDOSE
                || status == OrderStatus.DESPACHADA;
    }

    /**
     * Determina si el estado libera la mesa.
     *
     * @param status estado a evaluar.
     * @return true si debe liberar mesa.
     */
    private boolean shouldRelease(OrderStatus status) {
        return status == OrderStatus.PAGADA || status == OrderStatus.INACTIVA;
    }
}
