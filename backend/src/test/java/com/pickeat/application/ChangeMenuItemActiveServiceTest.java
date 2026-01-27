package com.pickeat.application;

import com.pickeat.application.usecase.ChangeMenuItemActiveService;
import com.pickeat.domain.DishType;
import com.pickeat.domain.MenuItem;
import com.pickeat.domain.MenuItemId;
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

class ChangeMenuItemActiveServiceTest {
    @Test
    void changesMenuItemActive() {
        MenuItemRepositoryPort repository = Mockito.mock(MenuItemRepositoryPort.class);
        ChangeMenuItemActiveService service = new ChangeMenuItemActiveService(repository);
        MenuItemId id = new MenuItemId(UUID.randomUUID());
        MenuItem existing = new MenuItem(
                id,
                "Descripcion larga",
                "Corta",
                "postre",
                DishType.POSTRE,
                false,
                false,
                true,
                BigDecimal.valueOf(4.0),
                null,
                Instant.now(),
                Instant.now()
        );

        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(repository.save(any(MenuItem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MenuItem updated = service.changeActive(id, true);

        assertThat(updated.isActive()).isTrue();
    }
}
