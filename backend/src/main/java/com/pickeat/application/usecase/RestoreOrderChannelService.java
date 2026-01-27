package com.pickeat.application.usecase;

import com.pickeat.domain.OrderChannel;
import com.pickeat.domain.OrderChannelId;
import com.pickeat.ports.in.RestoreOrderChannelUseCase;
import com.pickeat.ports.out.OrderChannelRepositoryPort;
import org.springframework.stereotype.Service;

/**
 * Servicio de aplicacion para restaurar canales de orden.
 */
@Service
public class RestoreOrderChannelService implements RestoreOrderChannelUseCase {
    private final OrderChannelRepositoryPort repository;

    public RestoreOrderChannelService(OrderChannelRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public OrderChannel restore(OrderChannelId id) {
        OrderChannel channel = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Canal no encontrado."));
        channel.setDeleted(false);
        channel.setActive(true);
        return repository.save(channel);
    }
}
