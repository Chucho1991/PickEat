package com.pickeat.application.usecase;

import com.pickeat.domain.Mesa;
import com.pickeat.domain.MesaId;
import com.pickeat.ports.in.RestoreMesaUseCase;
import com.pickeat.ports.out.MesaRepositoryPort;
import org.springframework.stereotype.Service;

/**
 * Caso de uso para restaurar mesas eliminadas lógicamente.
 */
@Service
public class RestoreMesaService implements RestoreMesaUseCase {
    private final MesaRepositoryPort repository;

    public RestoreMesaService(MesaRepositoryPort repository) {
        this.repository = repository;
    }

    /**
     * Restaura la mesa indicada.
     *
     * @param id identificador de la mesa.
     * @return mesa restaurada.
     */
    @Override
    public Mesa restore(MesaId id) {
        Mesa mesa = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Mesa no encontrada"));
        mesa.setDeleted(false);
        return repository.save(mesa);
    }
}
