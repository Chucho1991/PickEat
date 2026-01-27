package com.pickeat.application.usecase;

import com.pickeat.domain.Mesa;
import com.pickeat.domain.MesaId;
import com.pickeat.ports.in.UpdateMesaUseCase;
import com.pickeat.ports.out.MesaRepositoryPort;
import org.springframework.stereotype.Service;

/**
 * Caso de uso para actualizar mesas.
 */
@Service
public class UpdateMesaService implements UpdateMesaUseCase {
    private final MesaRepositoryPort repository;

    public UpdateMesaService(MesaRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public Mesa update(MesaId id, Mesa mesa) {
        Mesa existing = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Mesa no encontrada."));
        existing.setDescription(mesa.getDescription());
        existing.setSeats(mesa.getSeats());
        existing.setActive(mesa.isActive());
        existing.setOccupied(mesa.isOccupied());
        return repository.save(existing);
    }
}
