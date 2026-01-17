package com.pickeat.ports.out;

import com.pickeat.domain.MenuItemId;
import com.pickeat.domain.MenuItemImage;

/**
 * Puerto de salida para almacenamiento de imágenes de menú.
 */
public interface MenuItemImageStoragePort {
    /**
     * Guarda la imagen y retorna la ruta pública.
     *
     * @param id identificador del ítem.
     * @param image imagen a almacenar.
     * @return ruta pública de la imagen.
     */
    String store(MenuItemId id, MenuItemImage image);
}
