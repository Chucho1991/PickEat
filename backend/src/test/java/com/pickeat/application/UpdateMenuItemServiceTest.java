package com.pickeat.application;

import com.pickeat.application.usecase.UpdateMenuItemService;
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

class UpdateMenuItemServiceTest {
    @Test
    void updatesMenuItemDetails() {
        MenuItemRepositoryPort repository = Mockito.mock(MenuItemRepositoryPort.class);
        UpdateMenuItemService service = new UpdateMenuItemService(repository);
        MenuItemId id = new MenuItemId(UUID.randomUUID());
        MenuItem existing = new MenuItem(
                id,
                "Descripción larga",
                "Corta",
                "ensalada",
                DishType.ENTRADA,
                MenuItemStatus.INACTIVO,
                BigDecimal.valueOf(5),
                null,
                Instant.now(),
                Instant.now()
        );
        MenuItem update = new MenuItem(
                null,
                "Nueva descripción",
                "Nueva corta",
                "ensalada-fresca",
                DishType.ENTRADA,
                MenuItemStatus.ACTIVO,
                BigDecimal.valueOf(6.5),
                null,
                null,
                null
        );

        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(repository.findByNickname(update.getNickname())).thenReturn(Optional.empty());
        when(repository.save(any(MenuItem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MenuItem saved = service.update(id, update);

        assertThat(saved.getNickname()).isEqualTo("ensalada-fresca");
        assertThat(saved.getStatus()).isEqualTo(MenuItemStatus.ACTIVO);
    }
}
