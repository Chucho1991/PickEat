package com.pickeat.ports.in;

import com.pickeat.domain.MenuItem;
import com.pickeat.domain.MenuItemId;

public interface ChangeMenuItemActiveUseCase {
    MenuItem changeActive(MenuItemId id, boolean active);
}
