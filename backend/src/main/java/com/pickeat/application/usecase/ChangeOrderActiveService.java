package com.pickeat.application.usecase;

import com.pickeat.domain.Order;
import com.pickeat.domain.OrderId;
import com.pickeat.domain.OrderStatus;
import com.pickeat.ports.in.ChangeOrderActiveUseCase;
import com.pickeat.ports.out.MesaRepositoryPort;
import com.pickeat.ports.out.OrderRepositoryPort;
import org.springframework.stereotype.Service;

/**
 * Caso de uso para cambiar el estado activo de una orden.
 */
@Service
public class ChangeOrderActiveService implements ChangeOrderActiveUseCase {
    private final OrderRepositoryPort repository;
    private final MesaRepositoryPort mesaRepository;

    public ChangeOrderActiveService(OrderRepositoryPort repository, MesaRepositoryPort mesaRepository) {
        this.repository = repository;
        this.mesaRepository = mesaRepository;
    }

    /**
     * Cambia el estado activo de la orden indicada.
     *
     * @param id identificador de la orden.
     * @param activo nuevo estado.
     * @return orden actualizada.
     */
    @Override
    public Order changeActive(OrderId id, boolean activo) {
        Order order = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("La orden no existe."));
        if (order.isDeleted()) {
            throw new IllegalArgumentException("La orden esta eliminada.");
        }
        if (activo) {
            order.setStatus(order.getStatus() == OrderStatus.INACTIVA ? OrderStatus.CREADA : order.getStatus());
            order.setActive(true);
            if (order.getStatus() != OrderStatus.PAGADA) {
                mesaRepository.findById(order.getMesaId()).ifPresent(mesa -> {
                    if (!mesa.isOccupied()) {
                        mesa.setOccupied(true);
                        mesaRepository.save(mesa);
                    }
                });
            }
        } else {
            order.setStatus(OrderStatus.INACTIVA);
            order.setActive(false);
            mesaRepository.findById(order.getMesaId()).ifPresent(mesa -> {
                if (mesa.isOccupied()) {
                    mesa.setOccupied(false);
                    mesaRepository.save(mesa);
                }
            });
        }
        return repository.save(order);
    }
}
