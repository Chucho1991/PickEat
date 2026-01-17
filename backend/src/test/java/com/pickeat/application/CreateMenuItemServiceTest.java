package com.pickeat.application;

import com.pickeat.application.usecase.CreateMenuItemService;
import com.pickeat.domain.DishType;
import com.pickeat.domain.MenuItem;
import com.pickeat.ports.out.MenuItemRepositoryPort;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CreateMenuItemServiceTest {
    @Test
    void createsMenuItem() {
        MenuItemRepositoryPort repository = Mockito.mock(MenuItemRepositoryPort.class);
        CreateMenuItemService service = new CreateMenuItemService(repository);
        MenuItem menuItem = MenuItem.createNew(
                "Hamburguesa artesanal con doble queso",
                "Hamburguesa doble",
                "hamburguesa-doble",
                DishType.FUERTE,
                true,
                BigDecimal.valueOf(12.5)
        );

        when(repository.findByNickname(menuItem.getNickname())).thenReturn(java.util.Optional.empty());
        when(repository.save(any(MenuItem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MenuItem saved = service.create(menuItem);

        assertThat(saved.getNickname()).isEqualTo("hamburguesa-doble");
    }
}