package com.pickeat.application.usecase;

import com.pickeat.domain.MesaId;
import com.pickeat.ports.in.DeleteMesaUseCase;
import com.pickeat.ports.out.MesaRepositoryPort;
import org.springframework.stereotype.Service;

/**
 * Caso de uso para eliminar mesas.
 */
@Service
public class DeleteMesaService implements DeleteMesaUseCase {
    private final MesaRepositoryPort repository;

    public DeleteMesaService(MesaRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public void delete(MesaId id) {
        repository.findById(id).ifPresent(mesa -> {
            mesa.setActive(false);
            mesa.setDeleted(true);
            repository.save(mesa);
        });
    }
}
