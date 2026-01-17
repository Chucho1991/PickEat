package com.pickeat.ports.in;

import com.pickeat.domain.Mesa;
import com.pickeat.domain.MesaId;

public interface GetMesaUseCase {
    Mesa getById(MesaId id);
}
