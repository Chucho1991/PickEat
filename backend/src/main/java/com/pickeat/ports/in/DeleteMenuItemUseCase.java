package com.pickeat.ports.in;

import com.pickeat.domain.MenuItemId;

public interface DeleteMenuItemUseCase {
    void delete(MenuItemId id);
}
