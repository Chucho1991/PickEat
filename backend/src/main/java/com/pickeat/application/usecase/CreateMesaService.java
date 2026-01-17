package com.pickeat.application.usecase;

import com.pickeat.domain.Mesa;
import com.pickeat.ports.in.CreateMesaUseCase;
import com.pickeat.ports.out.MesaRepositoryPort;
import org.springframework.stereotype.Service;

/**
 * Caso de uso para crear mesas.
 */
@Service
public class CreateMesaService implements CreateMesaUseCase {
    private final MesaRepositoryPort repository;

    public CreateMesaService(MesaRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public Mesa create(Mesa mesa) {
        return repository.save(mesa);
    }
}
