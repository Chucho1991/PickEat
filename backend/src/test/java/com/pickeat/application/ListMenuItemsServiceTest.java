package com.pickeat.application;

import com.pickeat.application.usecase.ListMenuItemsService;
import com.pickeat.domain.DishType;
import com.pickeat.domain.MenuItem;
import com.pickeat.domain.MenuItemStatus;
import com.pickeat.ports.out.MenuItemRepositoryPort;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class ListMenuItemsServiceTest {
    @Test
    void listsMenuItemsWithFilters() {
        MenuItemRepositoryPort repository = Mockito.mock(MenuItemRepositoryPort.class);
        ListMenuItemsService service = new ListMenuItemsService(repository);

        when(repository.findAll(DishType.BEBIDA, MenuItemStatus.ACTIVO, "cafe"))
                .thenReturn(List.of(Mockito.mock(MenuItem.class)));

        List<MenuItem> items = service.list(DishType.BEBIDA, MenuItemStatus.ACTIVO, "cafe");

        assertThat(items).hasSize(1);
    }
}
