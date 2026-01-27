package com.pickeat.application.usecase;

import com.pickeat.domain.OrderId;
import com.pickeat.domain.OrderStatus;
import com.pickeat.ports.in.DeleteOrderUseCase;
import com.pickeat.ports.out.MesaRepositoryPort;
import com.pickeat.ports.out.OrderRepositoryPort;
import org.springframework.stereotype.Service;

/**
 * Caso de uso para eliminar ordenes de forma logica.
 */
@Service
public class DeleteOrderService implements DeleteOrderUseCase {
    private final OrderRepositoryPort repository;
    private final MesaRepositoryPort mesaRepository;

    public DeleteOrderService(OrderRepositoryPort repository, MesaRepositoryPort mesaRepository) {
        this.repository = repository;
        this.mesaRepository = mesaRepository;
    }

    /**
     * Marca la orden como eliminada.
     *
     * @param id identificador de la orden.
     */
    @Override
    public void delete(OrderId id) {
        repository.findById(id).ifPresent(order -> {
            order.setDeleted(true);
            order.setActive(false);
            order.setStatus(OrderStatus.ELIMINADA);
            repository.save(order);
            mesaRepository.findById(order.getMesaId()).ifPresent(mesa -> {
                if (mesa.isOccupied()) {
                    mesa.setOccupied(false);
                    mesaRepository.save(mesa);
                }
            });
        });
    }
}
