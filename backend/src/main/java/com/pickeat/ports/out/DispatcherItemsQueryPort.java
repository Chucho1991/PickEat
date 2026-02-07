package com.pickeat.ports.out;

import com.pickeat.domain.DispatcherItem;

import java.util.List;

/**
 * Puerto de salida para consultar platos del despachador desde persistencia.
 */
public interface DispatcherItemsQueryPort {
    List<DispatcherItem> listAll();
}
