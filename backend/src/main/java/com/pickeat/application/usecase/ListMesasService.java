package com.pickeat.application.usecase;

import com.pickeat.domain.Mesa;
import com.pickeat.ports.in.ListMesasUseCase;
import com.pickeat.ports.out.MesaRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Caso de uso para listar mesas.
 */
@Service
public class ListMesasService implements ListMesasUseCase {
    private final MesaRepositoryPort repository;

    public ListMesasService(MesaRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public List<Mesa> list(boolean includeDeleted) {
        return repository.findAll(includeDeleted);
    }
}
