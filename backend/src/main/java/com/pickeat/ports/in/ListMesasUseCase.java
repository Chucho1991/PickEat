package com.pickeat.ports.in;

import com.pickeat.domain.Mesa;

import java.util.List;

public interface ListMesasUseCase {
    List<Mesa> list(boolean includeDeleted);
}
