package com.pickeat.application.usecase;

import com.pickeat.domain.OrderChannel;
import com.pickeat.ports.in.CreateOrderChannelUseCase;
import com.pickeat.ports.out.OrderChannelRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * Servicio de aplicacion para crear canales de orden.
 */
@Service
public class CreateOrderChannelService implements CreateOrderChannelUseCase {
    private final OrderChannelRepositoryPort repository;

    public CreateOrderChannelService(OrderChannelRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public OrderChannel create(OrderChannel channel) {
        if (channel == null || channel.getName() == null || channel.getName().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        repository.findByName(channel.getName().trim()).ifPresent(existing -> {
            throw new IllegalArgumentException("El canal ya existe.");
        });
        channel.setName(channel.getName().trim());
        channel.setActive(true);
        channel.setDeleted(false);
        channel.setDefault(false);
        channel.setLocked(false);
        channel.setCreatedAt(Instant.now());
        channel.setUpdatedAt(Instant.now());
        return repository.save(channel);
    }
}
