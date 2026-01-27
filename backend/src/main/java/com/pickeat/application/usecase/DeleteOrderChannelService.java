package com.pickeat.application.usecase;

import com.pickeat.domain.OrderChannel;
import com.pickeat.domain.OrderChannelId;
import com.pickeat.ports.in.DeleteOrderChannelUseCase;
import com.pickeat.ports.out.OrderChannelRepositoryPort;
import org.springframework.stereotype.Service;

/**
 * Servicio de aplicacion para eliminar canales de orden.
 */
@Service
public class DeleteOrderChannelService implements DeleteOrderChannelUseCase {
    private final OrderChannelRepositoryPort repository;

    public DeleteOrderChannelService(OrderChannelRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public void delete(OrderChannelId id) {
        OrderChannel channel = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Canal no encontrado."));
        if (channel.isLocked()) {
            throw new IllegalArgumentException("El canal no se puede eliminar.");
        }
        channel.setDeleted(true);
        channel.setActive(false);
        repository.save(channel);
    }
}
