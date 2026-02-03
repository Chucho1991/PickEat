package com.pickeat.ports.in;

import com.pickeat.domain.DispatcherItem;

import java.util.List;

/**
 * Caso de uso para listar platos disponibles en el tablero del despachador.
 */
public interface ListDispatcherItemsUseCase {
    List<DispatcherItem> listAll();
}
