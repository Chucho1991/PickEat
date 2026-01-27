package com.pickeat.application.usecase;

import com.pickeat.domain.OrderChannel;
import com.pickeat.domain.OrderChannelId;
import com.pickeat.ports.in.UpdateOrderChannelUseCase;
import com.pickeat.ports.out.OrderChannelRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * Servicio de aplicacion para actualizar canales de orden.
 */
@Service
public class UpdateOrderChannelService implements UpdateOrderChannelUseCase {
    private final OrderChannelRepositoryPort repository;

    public UpdateOrderChannelService(OrderChannelRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public OrderChannel update(OrderChannelId id, OrderChannel channel) {
        OrderChannel existing = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Canal no encontrado."));
        if (existing.isLocked()) {
            throw new IllegalArgumentException("El canal no se puede modificar.");
        }
        if (channel.getName() == null || channel.getName().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        String name = channel.getName().trim();
        repository.findByName(name).ifPresent(found -> {
            if (!found.getId().equals(id)) {
                throw new IllegalArgumentException("El canal ya existe.");
            }
        });
        existing.setName(name);
        existing.setUpdatedAt(Instant.now());
        return repository.save(existing);
    }
}
