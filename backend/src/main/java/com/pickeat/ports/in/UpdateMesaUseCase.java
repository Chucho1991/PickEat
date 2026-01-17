package com.pickeat.ports.in;

import com.pickeat.domain.Mesa;
import com.pickeat.domain.MesaId;

public interface UpdateMesaUseCase {
    Mesa update(MesaId id, Mesa mesa);
}
