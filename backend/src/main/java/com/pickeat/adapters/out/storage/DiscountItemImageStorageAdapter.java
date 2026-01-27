package com.pickeat.adapters.out.storage;

import com.pickeat.domain.DiscountItemId;
import com.pickeat.domain.DiscountItemImage;
import com.pickeat.ports.out.DiscountItemImageStoragePort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Adaptador para almacenamiento local de imágenes de descuentos.
 */
@Component
public class DiscountItemImageStorageAdapter implements DiscountItemImageStoragePort {
    private final Path basePath;

    public DiscountItemImageStorageAdapter(@Value("${app.uploads.base-path:uploads}") String basePath) {
        this.basePath = Paths.get(basePath);
    }

    @Override
    public String store(DiscountItemId id, DiscountItemImage image) {
        String extension = resolveExtension(image);
        Path directory = basePath.resolve("discount-items");
        try {
            Files.createDirectories(directory);
            Path target = directory.resolve(id.getValue().toString() + extension);
            Files.write(target, image.getContent());
            return "/uploads/discount-items/" + id.getValue() + extension;
        } catch (IOException ex) {
            throw new IllegalArgumentException("No se pudo almacenar la imagen.");
        }
    }

    private String resolveExtension(DiscountItemImage image) {
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
