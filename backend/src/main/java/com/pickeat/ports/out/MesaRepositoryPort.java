package com.pickeat.ports.out;

import com.pickeat.domain.Mesa;
import com.pickeat.domain.MesaId;

import java.util.List;
import java.util.Optional;

public interface MesaRepositoryPort {
    Mesa save(Mesa mesa);

    Optional<Mesa> findById(MesaId id);

    List<Mesa> findAll(boolean includeDeleted);

    void deleteById(MesaId id);
}
