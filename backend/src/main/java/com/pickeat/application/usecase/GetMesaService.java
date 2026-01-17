package com.pickeat.application.usecase;

import com.pickeat.domain.Mesa;
import com.pickeat.domain.MesaId;
import com.pickeat.ports.in.GetMesaUseCase;
import com.pickeat.ports.out.MesaRepositoryPort;
import org.springframework.stereotype.Service;

/**
 * Caso de uso para consultar mesas.
 */
@Service
public class GetMesaService implements GetMesaUseCase {
    private final MesaRepositoryPort repository;

    public GetMesaService(MesaRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public Mesa getById(MesaId id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Mesa no encontrada."));
    }
}
