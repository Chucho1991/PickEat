package com.pickeat.application.usecase;

import com.pickeat.domain.MenuItem;
import com.pickeat.domain.MenuItemId;
import com.pickeat.domain.MenuItemImage;
import com.pickeat.ports.in.UploadMenuItemImageUseCase;
import com.pickeat.ports.out.MenuItemImageStoragePort;
import com.pickeat.ports.out.MenuItemRepositoryPort;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.Instant;

/**
 * Servicio de aplicación para subir imágenes del menú.
 */
@Service
public class UploadMenuItemImageService implements UploadMenuItemImageUseCase {
    private static final int REQUIRED_WIDTH = 400;
    private static final int REQUIRED_HEIGHT = 400;

    private final MenuItemRepositoryPort repository;
    private final MenuItemImageStoragePort storagePort;

    /**
     * Construye el servicio con sus dependencias.
     *
     * @param repository repositorio de menú.
     * @param storagePort almacenamiento de imágenes.
     */
    public UploadMenuItemImageService(MenuItemRepositoryPort repository,
                                      MenuItemImageStoragePort storagePort) {
        this.repository = repository;
        this.storagePort = storagePort;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MenuItem upload(MenuItemId id, MenuItemImage image) {
        MenuItem item = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ítem de menú no encontrado."));
        validateImage(image);
        String path = storagePort.store(id, image);
        item.setImagePath(path);
        item.setUpdatedAt(Instant.now());
        return repository.save(item);
    }

    /**
     * Valida tipo y dimensiones de la imagen.
     *
     * @param image imagen cargada.
     */
    private void validateImage(MenuItemImage image) {
        String contentType = image.getContentType();
        if (!"image/png".equalsIgnoreCase(contentType) && !"image/jpeg".equalsIgnoreCase(contentType)) {
            throw new IllegalArgumentException("La imagen debe ser PNG o JPG.");
        }
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(image.getContent())) {
            BufferedImage bufferedImage = ImageIO.read(inputStream);
            if (bufferedImage == null) {
                throw new IllegalArgumentException("La imagen no es válida.");
            }
            if (bufferedImage.getWidth() != REQUIRED_WIDTH || bufferedImage.getHeight() != REQUIRED_HEIGHT) {
                throw new IllegalArgumentException("La imagen debe ser de 400x400 px.");
            }
        } catch (IOException ex) {
            throw new IllegalArgumentException("No se pudo leer la imagen.");
        }
    }
}
