package com.pickeat.adapters.out.storage;

import com.pickeat.domain.MenuItemId;
import com.pickeat.domain.MenuItemImage;
import com.pickeat.ports.out.MenuItemImageStoragePort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Adaptador para almacenamiento local de imágenes de menú.
 */
@Component
public class MenuItemImageStorageAdapter implements MenuItemImageStoragePort {
    private final Path basePath;

    /**
     * Construye el adaptador con la ruta base.
     *
     * @param basePath ruta base de uploads.
     */
    public MenuItemImageStorageAdapter(@Value("${app.uploads.base-path:uploads}") String basePath) {
        this.basePath = Paths.get(basePath);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String store(MenuItemId id, MenuItemImage image) {
        String extension = resolveExtension(image);
        Path directory = basePath.resolve("menu-items");
        try {
            Files.createDirectories(directory);
            Path target = directory.resolve(id.getValue().toString() + extension);
            Files.write(target, image.getContent());
            return "/uploads/menu-items/" + id.getValue() + extension;
        } catch (IOException ex) {
            throw new IllegalArgumentException("No se pudo almacenar la imagen.");
        }
    }

    /**
     * Determina la extensión según el tipo de contenido.
     *
     * @param image imagen a evaluar.
     * @return extensión con punto.
     */
    private String resolveExtension(MenuItemImage image) {
        String contentType = image.getContentType();
        if ("image/png".equalsIgnoreCase(contentType)) {
            return ".png";
        }
        if ("image/jpeg".equalsIgnoreCase(contentType)) {
            return ".jpg";
        }
        String filename = image.getOriginalFilename();
        if (filename != null && filename.toLowerCase().endsWith(".jpg")) {
            return ".jpg";
        }
        if (filename != null && filename.toLowerCase().endsWith(".png")) {
            return ".png";
        }
        throw new IllegalArgumentException("La imagen debe ser PNG o JPG.");
    }
}
