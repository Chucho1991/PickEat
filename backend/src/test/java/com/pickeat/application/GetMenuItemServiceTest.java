package com.pickeat.application;

import com.pickeat.application.usecase.GetMenuItemService;
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
import static org.mockito.Mockito.when;

class GetMenuItemServiceTest {
    @Test
    void getsMenuItemById() {
        MenuItemRepositoryPort repository = Mockito.mock(MenuItemRepositoryPort.class);
        GetMenuItemService service = new GetMenuItemService(repository);
        MenuItemId id = new MenuItemId(UUID.randomUUID());
        MenuItem existing = new MenuItem(
                id,
                "Descripcion larga",
                "Corta",
                "sopa",
                DishType.ENTRADA,
                true,
                false,
                BigDecimal.valueOf(3.5),
                null,
                Instant.now(),
                Instant.now()
        );

        when(repository.findById(id)).thenReturn(Optional.of(existing));

        MenuItem found = service.getById(id);

        assertThat(found.getNickname()).isEqualTo("sopa");
    }
}