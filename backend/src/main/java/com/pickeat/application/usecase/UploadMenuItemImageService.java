package com.pickeat.application.usecase;

import com.pickeat.domain.MenuItem;
import com.pickeat.domain.MenuItemId;
import com.pickeat.domain.MenuItemImage;
import com.pickeat.ports.in.UploadMenuItemImageUseCase;
import com.pickeat.ports.out.MenuItemImageStoragePort;
import com.pickeat.ports.out.MenuItemRepositoryPort;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;

/**
 * Servicio de aplicacion para subir imagenes del menu.
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
     * @param repository repositorio de menu.
     * @param storagePort almacenamiento de imagenes.
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
                .orElseThrow(() -> new IllegalArgumentException("Item de menu no encontrado."));
        MenuItemImage normalizedImage = normalizeImage(image);
        String path = storagePort.store(id, normalizedImage);
        item.setImagePath(path);
        item.setUpdatedAt(Instant.now());
        return repository.save(item);
    }

    /**
     * Valida tipo y redimensiona la imagen.
     *
     * @param image imagen cargada.
     */
    private MenuItemImage normalizeImage(MenuItemImage image) {
        String contentType = image.getContentType();
        if (!isSupportedContentType(contentType)) {
            throw new IllegalArgumentException("La imagen debe ser PNG o JPG.");
        }
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(image.getContent())) {
            BufferedImage bufferedImage = ImageIO.read(inputStream);
            if (bufferedImage == null) {
                throw new IllegalArgumentException("La imagen no es valida.");
            }
            BufferedImage resized = resizeImage(bufferedImage);
            String outputFormat = resolveOutputFormat(resized);
            String outputContentType = "png".equals(outputFormat) ? "image/png" : "image/jpeg";
            byte[] content = writeImage(resized, outputFormat);
            return new MenuItemImage(content, outputContentType, image.getOriginalFilename());
        } catch (IOException ex) {
            throw new IllegalArgumentException("No se pudo leer la imagen.");
        }
    }

    private boolean isSupportedContentType(String contentType) {
        if (contentType == null) {
            return false;
        }
        return "image/png".equalsIgnoreCase(contentType)
                || "image/jpeg".equalsIgnoreCase(contentType)
                || "image/jpg".equalsIgnoreCase(contentType)
                || "image/pjpeg".equalsIgnoreCase(contentType)
                || "image/webp".equalsIgnoreCase(contentType);
    }

    private BufferedImage resizeImage(BufferedImage source) {
        int imageType = source.getColorModel().hasAlpha()
                ? BufferedImage.TYPE_INT_ARGB
                : BufferedImage.TYPE_INT_RGB;
        BufferedImage target = new BufferedImage(REQUIRED_WIDTH, REQUIRED_HEIGHT, imageType);
        Graphics2D graphics = target.createGraphics();
        try {
            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (imageType == BufferedImage.TYPE_INT_RGB) {
                graphics.setBackground(Color.WHITE);
                graphics.clearRect(0, 0, REQUIRED_WIDTH, REQUIRED_HEIGHT);
            }
            graphics.drawImage(source, 0, 0, REQUIRED_WIDTH, REQUIRED_HEIGHT, null);
            return target;
        } finally {
            graphics.dispose();
        }
    }

    private String resolveOutputFormat(BufferedImage image) {
        return image.getColorModel().hasAlpha() ? "png" : "jpg";
    }

    private byte[] writeImage(BufferedImage image, String format) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        if (!ImageIO.write(image, format, outputStream)) {
            throw new IOException("No se pudo escribir la imagen.");
        }
        return outputStream.toByteArray();
    }
}
