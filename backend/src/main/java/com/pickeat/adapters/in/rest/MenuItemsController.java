package com.pickeat.adapters.in.rest;

import com.pickeat.adapters.in.rest.dto.MenuItemRequest;
import com.pickeat.adapters.in.rest.dto.MenuItemResponse;
import com.pickeat.adapters.in.rest.dto.MenuItemStatusRequest;
import com.pickeat.adapters.in.rest.mapper.MenuItemRestMapper;
import com.pickeat.domain.DishType;
import com.pickeat.domain.MenuItemId;
import com.pickeat.domain.MenuItemImage;
import com.pickeat.domain.MenuItemStatus;
import com.pickeat.ports.in.ChangeMenuItemStatusUseCase;
import com.pickeat.ports.in.CreateMenuItemUseCase;
import com.pickeat.ports.in.GetMenuItemUseCase;
import com.pickeat.ports.in.ListMenuItemsUseCase;
import com.pickeat.ports.in.UpdateMenuItemUseCase;
import com.pickeat.ports.in.UploadMenuItemImageUseCase;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
 * Controlador REST para la administración del menú.
 */
@RestController
@RequestMapping("/menu-items")
public class MenuItemsController {
    private final CreateMenuItemUseCase createMenuItemUseCase;
    private final UpdateMenuItemUseCase updateMenuItemUseCase;
    private final GetMenuItemUseCase getMenuItemUseCase;
    private final ListMenuItemsUseCase listMenuItemsUseCase;
    private final ChangeMenuItemStatusUseCase changeMenuItemStatusUseCase;
    private final UploadMenuItemImageUseCase uploadMenuItemImageUseCase;
    private final MenuItemRestMapper mapper = new MenuItemRestMapper();

    /**
     * Construye el controlador con sus dependencias.
     *
     * @param createMenuItemUseCase caso de uso de creación.
     * @param updateMenuItemUseCase caso de uso de actualización.
     * @param getMenuItemUseCase caso de uso de consulta.
     * @param listMenuItemsUseCase caso de uso de listado.
     * @param changeMenuItemStatusUseCase caso de uso de estado.
     * @param uploadMenuItemImageUseCase caso de uso de imagen.
     */
    public MenuItemsController(CreateMenuItemUseCase createMenuItemUseCase,
                               UpdateMenuItemUseCase updateMenuItemUseCase,
                               GetMenuItemUseCase getMenuItemUseCase,
                               ListMenuItemsUseCase listMenuItemsUseCase,
                               ChangeMenuItemStatusUseCase changeMenuItemStatusUseCase,
                               UploadMenuItemImageUseCase uploadMenuItemImageUseCase) {
        this.createMenuItemUseCase = createMenuItemUseCase;
        this.updateMenuItemUseCase = updateMenuItemUseCase;
        this.getMenuItemUseCase = getMenuItemUseCase;
        this.listMenuItemsUseCase = listMenuItemsUseCase;
        this.changeMenuItemStatusUseCase = changeMenuItemStatusUseCase;
        this.uploadMenuItemImageUseCase = uploadMenuItemImageUseCase;
    }

    /**
     * Crea un ítem del menú.
     *
     * @param request datos del ítem.
     * @return respuesta con el ítem creado.
     */
    @PostMapping
    public ResponseEntity<MenuItemResponse> create(@Valid @RequestBody MenuItemRequest request) {
        return ResponseEntity.ok(mapper.toResponse(createMenuItemUseCase.create(mapper.toDomain(request))));
    }

    /**
     * Actualiza un ítem del menú.
     *
     * @param id identificador.
     * @param request datos actualizados.
     * @return respuesta con el ítem actualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MenuItemResponse> update(@PathVariable("id") UUID id,
                                                   @Valid @RequestBody MenuItemRequest request) {
        return ResponseEntity.ok(
                mapper.toResponse(updateMenuItemUseCase.update(new MenuItemId(id), mapper.toUpdateDomain(request)))
        );
    }

    /**
     * Obtiene un ítem por id.
     *
     * @param id identificador.
     * @return respuesta con el ítem.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MenuItemResponse> getById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(mapper.toResponse(getMenuItemUseCase.getById(new MenuItemId(id))));
    }

    /**
     * Lista ítems del menú con filtros.
     *
     * @param dishType filtro por tipo.
     * @param status filtro por estado.
     * @param search búsqueda libre.
     * @return listado de ítems.
     */
    @GetMapping
    public ResponseEntity<List<MenuItemResponse>> list(@RequestParam(value = "dishType", required = false) DishType dishType,
                                                       @RequestParam(value = "status", required = false) MenuItemStatus status,
                                                       @RequestParam(value = "search", required = false) String search) {
        List<MenuItemResponse> items = listMenuItemsUseCase.list(dishType, status, search)
                .stream()
                .map(mapper::toResponse)
                .toList();
        return ResponseEntity.ok(items);
    }

    /**
     * Cambia el estado del ítem.
     *
     * @param id identificador.
     * @param request solicitud de estado.
     * @return ítem actualizado.
     */
    @PostMapping("/{id}/status")
    public ResponseEntity<MenuItemResponse> changeStatus(@PathVariable("id") UUID id,
                                                         @Valid @RequestBody MenuItemStatusRequest request) {
        return ResponseEntity.ok(
                mapper.toResponse(changeMenuItemStatusUseCase.changeStatus(new MenuItemId(id), request.getStatus()))
        );
    }

    /**
     * Carga la imagen del ítem.
     *
     * @param id identificador del ítem.
     * @param file archivo subido.
     * @return ítem actualizado.
     */
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
}
