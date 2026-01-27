package com.pickeat.adapters.in.rest;

import com.pickeat.adapters.in.rest.dto.DiscountItemActiveRequest;
import com.pickeat.adapters.in.rest.dto.DiscountItemRequest;
import com.pickeat.adapters.in.rest.dto.DiscountItemResponse;
import com.pickeat.adapters.in.rest.mapper.DiscountItemRestMapper;
import com.pickeat.config.SecurityUtils;
import com.pickeat.domain.DiscountItem;
import com.pickeat.domain.DiscountItemId;
import com.pickeat.domain.DiscountItemImage;
import com.pickeat.ports.in.ChangeDiscountItemActiveUseCase;
import com.pickeat.ports.in.CreateDiscountItemUseCase;
import com.pickeat.ports.in.DeleteDiscountItemUseCase;
import com.pickeat.ports.in.GetDiscountItemUseCase;
import com.pickeat.ports.in.ListDiscountItemsUseCase;
import com.pickeat.ports.in.RestoreDiscountItemUseCase;
import com.pickeat.ports.in.UpdateDiscountItemUseCase;
import com.pickeat.ports.in.UploadDiscountItemImageUseCase;
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
 * Controlador REST para administración de descuentos.
 */
@RestController
@RequestMapping("/discount-items")
public class DiscountItemsController {
    private final CreateDiscountItemUseCase createDiscountItemUseCase;
    private final UpdateDiscountItemUseCase updateDiscountItemUseCase;
    private final GetDiscountItemUseCase getDiscountItemUseCase;
    private final ListDiscountItemsUseCase listDiscountItemsUseCase;
    private final ChangeDiscountItemActiveUseCase changeDiscountItemActiveUseCase;
    private final DeleteDiscountItemUseCase deleteDiscountItemUseCase;
    private final RestoreDiscountItemUseCase restoreDiscountItemUseCase;
    private final UploadDiscountItemImageUseCase uploadDiscountItemImageUseCase;
    private final DiscountItemRestMapper mapper = new DiscountItemRestMapper();

    public DiscountItemsController(CreateDiscountItemUseCase createDiscountItemUseCase,
                                   UpdateDiscountItemUseCase updateDiscountItemUseCase,
                                   GetDiscountItemUseCase getDiscountItemUseCase,
                                   ListDiscountItemsUseCase listDiscountItemsUseCase,
                                   ChangeDiscountItemActiveUseCase changeDiscountItemActiveUseCase,
                                   DeleteDiscountItemUseCase deleteDiscountItemUseCase,
                                   RestoreDiscountItemUseCase restoreDiscountItemUseCase,
                                   UploadDiscountItemImageUseCase uploadDiscountItemImageUseCase) {
        this.createDiscountItemUseCase = createDiscountItemUseCase;
        this.updateDiscountItemUseCase = updateDiscountItemUseCase;
        this.getDiscountItemUseCase = getDiscountItemUseCase;
        this.listDiscountItemsUseCase = listDiscountItemsUseCase;
        this.changeDiscountItemActiveUseCase = changeDiscountItemActiveUseCase;
        this.deleteDiscountItemUseCase = deleteDiscountItemUseCase;
        this.restoreDiscountItemUseCase = restoreDiscountItemUseCase;
        this.uploadDiscountItemImageUseCase = uploadDiscountItemImageUseCase;
    }

    @PostMapping
    public ResponseEntity<DiscountItemResponse> create(@Valid @RequestBody DiscountItemRequest request) {
        return ResponseEntity.ok(mapper.toResponse(createDiscountItemUseCase.create(mapper.toDomain(request))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DiscountItemResponse> update(@PathVariable("id") UUID id,
                                                       @Valid @RequestBody DiscountItemRequest request) {
        if (!isSuperadmin()) {
            DiscountItem existing = getDiscountItemUseCase.getById(new DiscountItemId(id));
            if (existing.isDeleted()) {
                return ResponseEntity.notFound().build();
            }
            request.setActivo(existing.isActive());
        }
        return ResponseEntity.ok(
                mapper.toResponse(updateDiscountItemUseCase.update(new DiscountItemId(id), mapper.toUpdateDomain(request)))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiscountItemResponse> getById(@PathVariable("id") UUID id) {
        DiscountItemResponse response = mapper.toResponse(getDiscountItemUseCase.getById(new DiscountItemId(id)));
        if (response.isDeleted() && !isSuperadmin()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<DiscountItemResponse>> list(@RequestParam(value = "activo", required = false) Boolean activo,
                                                           @RequestParam(value = "search", required = false) String search,
                                                           @RequestParam(value = "includeDeleted", required = false) Boolean includeDeleted) {
        boolean includeDeletedResolved = Boolean.TRUE.equals(includeDeleted) && isSuperadmin();
        List<DiscountItemResponse> items = listDiscountItemsUseCase.list(activo, search, includeDeletedResolved)
                .stream()
                .map(mapper::toResponse)
                .toList();
        return ResponseEntity.ok(items);
    }

    @PostMapping("/{id}/active")
    @PreAuthorize("hasRole('SUPERADMINISTRADOR')")
    public ResponseEntity<DiscountItemResponse> changeActive(@PathVariable("id") UUID id,
                                                             @Valid @RequestBody DiscountItemActiveRequest request) {
        return ResponseEntity.ok(
                mapper.toResponse(changeDiscountItemActiveUseCase.changeActive(new DiscountItemId(id),
                        Boolean.TRUE.equals(request.getActivo())))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        deleteDiscountItemUseCase.delete(new DiscountItemId(id));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/restore")
    public ResponseEntity<DiscountItemResponse> restore(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(mapper.toResponse(restoreDiscountItemUseCase.restore(new DiscountItemId(id))));
    }

    @PostMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DiscountItemResponse> uploadImage(@PathVariable("id") UUID id,
                                                            @RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("La imagen es obligatoria.");
        }
        try {
            DiscountItemImage image = new DiscountItemImage(file.getBytes(), file.getContentType(), file.getOriginalFilename());
            return ResponseEntity.ok(
                    mapper.toResponse(uploadDiscountItemImageUseCase.upload(new DiscountItemId(id), image))
            );
        } catch (IOException ex) {
            throw new IllegalArgumentException("No se pudo leer la imagen.");
        }
    }

    private boolean isSuperadmin() {
        return "SUPERADMINISTRADOR".equals(SecurityUtils.currentRole());
    }
}
