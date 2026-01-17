package com.pickeat.application;

import com.pickeat.application.usecase.ChangeMenuItemStatusService;
import com.pickeat.domain.DishType;
import com.pickeat.domain.MenuItem;
import com.pickeat.domain.MenuItemId;
import com.pickeat.domain.MenuItemStatus;
import com.pickeat.ports.out.MenuItemRepositoryPort;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ChangeMenuItemStatusServiceTest {
    @Test
    void changesMenuItemStatus() {
        MenuItemRepositoryPort repository = Mockito.mock(MenuItemRepositoryPort.class);
        ChangeMenuItemStatusService service = new ChangeMenuItemStatusService(repository);
        MenuItemId id = new MenuItemId(UUID.randomUUID());
        MenuItem existing = new MenuItem(
                id,
                "Descripción larga",
                "Corta",
                "postre",
                DishType.POSTRE,
                MenuItemStatus.INACTIVO,
                BigDecimal.valueOf(4.0),
                null,
                Instant.now(),
                Instant.now()
        );

        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(repository.save(any(MenuItem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MenuItem updated = service.changeStatus(id, MenuItemStatus.ACTIVO);

        assertThat(updated.getStatus()).isEqualTo(MenuItemStatus.ACTIVO);
    }
}
