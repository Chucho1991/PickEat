package com.pickeat.application.usecase;

import com.pickeat.domain.DiscountItem;
import com.pickeat.domain.DiscountItemId;
import com.pickeat.domain.DiscountItemImage;
import com.pickeat.ports.in.UploadDiscountItemImageUseCase;
import com.pickeat.ports.out.DiscountItemImageStoragePort;
import com.pickeat.ports.out.DiscountItemRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * Servicio de aplicación para cargar imágenes de descuentos.
 */
@Service
public class UploadDiscountItemImageService implements UploadDiscountItemImageUseCase {
    private final DiscountItemRepositoryPort repository;
    private final DiscountItemImageStoragePort storagePort;

    public UploadDiscountItemImageService(DiscountItemRepositoryPort repository,
                                          DiscountItemImageStoragePort storagePort) {
        this.repository = repository;
        this.storagePort = storagePort;
    }

    @Override
    public DiscountItem upload(DiscountItemId id, DiscountItemImage image) {
        DiscountItem item = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Descuento no encontrado."));
        if (item.isDeleted()) {
            throw new IllegalArgumentException("El descuento esta eliminado.");
        }
        String imagePath = storagePort.store(id, image);
        item.setImagePath(imagePath);
        item.setUpdatedAt(Instant.now());
        return repository.save(item);
    }
}
