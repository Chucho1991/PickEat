package com.pickeat.application;

import com.pickeat.application.usecase.UploadMenuItemImageService;
import com.pickeat.domain.DishType;
import com.pickeat.domain.MenuItem;
import com.pickeat.domain.MenuItemId;
import com.pickeat.domain.MenuItemImage;
import com.pickeat.ports.out.MenuItemImageStoragePort;
import com.pickeat.ports.out.MenuItemRepositoryPort;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UploadMenuItemImageServiceTest {
    @Test
    void uploadsMenuItemImage() throws Exception {
        MenuItemRepositoryPort repository = Mockito.mock(MenuItemRepositoryPort.class);
        MenuItemImageStoragePort storagePort = Mockito.mock(MenuItemImageStoragePort.class);
        UploadMenuItemImageService service = new UploadMenuItemImageService(repository, storagePort);
        MenuItemId id = new MenuItemId(UUID.randomUUID());
        MenuItem existing = new MenuItem(
                id,
                "Descripcion larga",
                "Corta",
                "combo",
                DishType.COMBO,
                true,
                false,
                BigDecimal.valueOf(9.99),
                null,
                Instant.now(),
                Instant.now()
        );

        BufferedImage image = new BufferedImage(300, 500, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", outputStream);
        MenuItemImage menuImage = new MenuItemImage(outputStream.toByteArray(), "image/png", "combo.png");

        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(storagePort.store(eq(id), any(MenuItemImage.class)))
                .thenReturn("/uploads/menu-items/" + id.getValue() + ".png");
        when(repository.save(any(MenuItem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MenuItem updated = service.upload(id, menuImage);

        ArgumentCaptor<MenuItemImage> imageCaptor = ArgumentCaptor.forClass(MenuItemImage.class);
        verify(storagePort).store(eq(id), imageCaptor.capture());
        assertThat(updated.getImagePath()).contains("/uploads/menu-items/");

        MenuItemImage storedImage = imageCaptor.getValue();
        BufferedImage storedBuffered = ImageIO.read(new ByteArrayInputStream(storedImage.getContent()));
        assertThat(storedBuffered.getWidth()).isEqualTo(400);
        assertThat(storedBuffered.getHeight()).isEqualTo(400);
    }
}