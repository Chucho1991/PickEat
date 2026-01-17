package com.pickeat.application.usecase;

import com.pickeat.domain.Mesa;
import com.pickeat.domain.MesaId;
import com.pickeat.ports.in.ChangeMesaActiveUseCase;
import com.pickeat.ports.out.MesaRepositoryPort;
import org.springframework.stereotype.Service;

/**
 * Caso de uso para activar o inactivar mesas.
 */
@Service
public class ChangeMesaActiveService implements ChangeMesaActiveUseCase {
    private final MesaRepositoryPort repository;

    public ChangeMesaActiveService(MesaRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public Mesa changeActive(MesaId id, boolean active) {
        Mesa mesa = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Mesa no encontrada."));
        mesa.setActive(active);
        return repository.save(mesa);
    }
}
