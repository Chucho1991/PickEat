package com.pickeat.adapters.in.rest;

import com.pickeat.adapters.in.rest.dto.MenuItemActiveRequest;
import com.pickeat.adapters.in.rest.dto.MenuItemRequest;
import com.pickeat.adapters.in.rest.dto.MenuItemResponse;
import com.pickeat.adapters.in.rest.mapper.MenuItemRestMapper;
import com.pickeat.config.SecurityUtils;
import com.pickeat.domain.DishType;
import com.pickeat.domain.MenuItem;
import com.pickeat.domain.MenuItemId;
import com.pickeat.domain.MenuItemImage;
import com.pickeat.ports.in.ChangeMenuItemActiveUseCase;
import com.pickeat.ports.in.CreateMenuItemUseCase;
import com.pickeat.ports.in.DeleteMenuItemUseCase;
import com.pickeat.ports.in.GetMenuItemUseCase;
import com.pickeat.ports.in.ListMenuItemsUseCase;
import com.pickeat.ports.in.RestoreMenuItemUseCase;
import com.pickeat.ports.in.UpdateMenuItemUseCase;
import com.pickeat.ports.in.UploadMenuItemImageUseCase;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Controlador REST para la administracion del menu.
 */
@RestController
@RequestMapping("/menu-items")
public class MenuItemsController {
    private final CreateMenuItemUseCase createMenuItemUseCase;
    private final UpdateMenuItemUseCase updateMenuItemUseCase;
    private final GetMenuItemUseCase getMenuItemUseCase;
    private final ListMenuItemsUseCase listMenuItemsUseCase;
    private final ChangeMenuItemActiveUseCase changeMenuItemActiveUseCase;
    private final DeleteMenuItemUseCase deleteMenuItemUseCase;
    private final RestoreMenuItemUseCase restoreMenuItemUseCase;
    private final UploadMenuItemImageUseCase uploadMenuItemImageUseCase;
    private final MenuItemRestMapper mapper = new MenuItemRestMapper();

    public MenuItemsController(CreateMenuItemUseCase createMenuItemUseCase,
                               UpdateMenuItemUseCase updateMenuItemUseCase,
                               GetMenuItemUseCase getMenuItemUseCase,
                               ListMenuItemsUseCase listMenuItemsUseCase,
                               ChangeMenuItemActiveUseCase changeMenuItemActiveUseCase,
                               DeleteMenuItemUseCase deleteMenuItemUseCase,
                               RestoreMenuItemUseCase restoreMenuItemUseCase,
                               UploadMenuItemImageUseCase uploadMenuItemImageUseCase) {
        this.createMenuItemUseCase = createMenuItemUseCase;
        this.updateMenuItemUseCase = updateMenuItemUseCase;
        this.getMenuItemUseCase = getMenuItemUseCase;
        this.listMenuItemsUseCase = listMenuItemsUseCase;
        this.changeMenuItemActiveUseCase = changeMenuItemActiveUseCase;
        this.deleteMenuItemUseCase = deleteMenuItemUseCase;
        this.restoreMenuItemUseCase = restoreMenuItemUseCase;
        this.uploadMenuItemImageUseCase = uploadMenuItemImageUseCase;
    }

    @PostMapping
    public ResponseEntity<MenuItemResponse> create(@Valid @RequestBody MenuItemRequest request) {
        return ResponseEntity.ok(mapper.toResponse(createMenuItemUseCase.create(mapper.toDomain(request))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuItemResponse> update(@PathVariable("id") UUID id,
                                                   @Valid @RequestBody MenuItemRequest request) {
        if (!isSuperadmin()) {
            MenuItem existing = getMenuItemUseCase.getById(new MenuItemId(id));
            if (existing.isDeleted()) {
                return ResponseEntity.notFound().build();
            }
            request.setActivo(existing.isActive());
        }
        return ResponseEntity.ok(
                mapper.toResponse(updateMenuItemUseCase.update(new MenuItemId(id), mapper.toUpdateDomain(request)))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuItemResponse> getById(@PathVariable("id") UUID id) {
        MenuItemResponse response = mapper.toResponse(getMenuItemUseCase.getById(new MenuItemId(id)));
        if (response.isDeleted() && !isSuperadmin()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<MenuItemResponse>> list(@RequestParam(value = "dishType", required = false) DishType dishType,
                                                       @RequestParam(value = "activo", required = false) Boolean activo,
                                                       @RequestParam(value = "search", required = false) String search,
                                                       @RequestParam(value = "includeDeleted", required = false) Boolean includeDeleted) {
        boolean includeDeletedResolved = Boolean.TRUE.equals(includeDeleted) && isSuperadmin();
        List<MenuItemResponse> items = listMenuItemsUseCase.list(dishType, activo, search, includeDeletedResolved)
                .stream()
                .map(mapper::toResponse)
                .toList();
        return ResponseEntity.ok(items);
    }

    @PostMapping("/{id}/active")
    @PreAuthorize("hasRole('SUPERADMINISTRADOR')")
    public ResponseEntity<MenuItemResponse> changeActive(@PathVariable("id") UUID id,
                                                         @Valid @RequestBody MenuItemActiveRequest request) {
        return ResponseEntity.ok(
                mapper.toResponse(changeMenuItemActiveUseCase.changeActive(new MenuItemId(id),
                        Boolean.TRUE.equals(request.getActivo())))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        deleteMenuItemUseCase.delete(new MenuItemId(id));
        return ResponseEntity.noContent().build();
    }

    /**
     * Restaura un item del menu eliminado lógicamente.
     *
     * @param id identificador del item.
     * @return respuesta con el item restaurado.
     */
    @PostMapping("/{id}/restore")
    public ResponseEntity<MenuItemResponse> restore(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(mapper.toResponse(restoreMenuItemUseCase.restore(new MenuItemId(id))));
    }

    @PostMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MenuItemResponse> uploadImage(@PathVariable("id") UUID id,
                                                        @RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("La imagen es obligatoria.");
        }
        try {
            MenuItemImage image = new MenuItemImage(file.getBytes(), file.getContentType(), file.getOriginalFilename());
            return ResponseEntity.ok(
                    mapper.toResponse(uploadMenuItemImageUseCase.upload(new MenuItemId(id), image))
            );
        } catch (IOException ex) {
            throw new IllegalArgumentException("No se pudo leer la imagen.");
        }
    }

    private boolean isSuperadmin() {
        return "SUPERADMINISTRADOR".equals(SecurityUtils.currentRole());
    }
}
