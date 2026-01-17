package com.pickeat.ports.in;

import com.pickeat.domain.MenuItem;
import com.pickeat.domain.MenuItemId;
import com.pickeat.domain.MenuItemImage;

/**
 * Caso de uso para cargar imagenes del menú.
 */
public interface UploadMenuItemImageUseCase {
    /**
     * Sube la imagen para un ítem existente.
     *
     * @param id identificador del ítem.
     * @param image imagen cargada.
     * @return ítem actualizado con la ruta de imagen.
     */
    MenuItem upload(MenuItemId id, MenuItemImage image);
}
