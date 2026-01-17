package com.pickeat.ports.in;

import com.pickeat.domain.Mesa;
import com.pickeat.domain.MesaId;

public interface ChangeMesaActiveUseCase {
    Mesa changeActive(MesaId id, boolean active);
}
