package com.pickeat.application.usecase;

import com.pickeat.domain.DispatcherItem;
import com.pickeat.ports.out.DispatcherItemsQueryPort;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias para el caso de uso de listado de despachador.
 */
class ListDispatcherItemsServiceTest {
    @Test
    void listAllDelegatesToPort() {
        DispatcherItemsQueryPort port = mock(DispatcherItemsQueryPort.class);
        List<DispatcherItem> expected = List.of(
                new DispatcherItem("ORD-1", "Mesa 2", "Café", 1, Instant.parse("2024-01-01T10:00:00Z"))
        );
        when(port.listAll()).thenReturn(expected);
        ListDispatcherItemsService service = new ListDispatcherItemsService(port);

        List<DispatcherItem> result = service.listAll();

        assertThat(result).isEqualTo(expected);
        verify(port).listAll();
    }
}
