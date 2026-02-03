package com.pickeat.application.usecase;

import com.pickeat.domain.DispatcherItem;
import com.pickeat.ports.in.ListDispatcherItemsUseCase;
import com.pickeat.ports.out.DispatcherItemsQueryPort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Caso de uso para listar los platos visibles en el tablero del despachador.
 */
@Service
public class ListDispatcherItemsService implements ListDispatcherItemsUseCase {
    private final DispatcherItemsQueryPort dispatcherItemsQueryPort;

    public ListDispatcherItemsService(DispatcherItemsQueryPort dispatcherItemsQueryPort) {
        this.dispatcherItemsQueryPort = dispatcherItemsQueryPort;
    }

    @Override
    public List<DispatcherItem> listAll() {
        return dispatcherItemsQueryPort.listAll();
    }
}
