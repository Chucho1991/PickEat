package com.pickeat.adapters.in.rest;

import com.pickeat.adapters.in.rest.dto.MesaActiveRequest;
import com.pickeat.adapters.in.rest.dto.MesaRequest;
import com.pickeat.adapters.in.rest.dto.MesaResponse;
import com.pickeat.adapters.in.rest.mapper.MesaRestMapper;
import com.pickeat.config.SecurityUtils;
import com.pickeat.domain.Mesa;
import com.pickeat.domain.MesaId;
import com.pickeat.ports.in.ChangeMesaActiveUseCase;
import com.pickeat.ports.in.CreateMesaUseCase;
import com.pickeat.ports.in.DeleteMesaUseCase;
import com.pickeat.ports.in.GetMesaUseCase;
import com.pickeat.ports.in.ListMesasUseCase;
import com.pickeat.ports.in.RestoreMesaUseCase;
import com.pickeat.ports.in.UpdateMesaUseCase;
import jakarta.validation.Valid;
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

import java.util.List;
import java.util.UUID;

/**
 * Controlador REST para operaciones relacionadas con mesas.
 */
@RestController
@RequestMapping("/mesas")
public class MesasController {
    private final CreateMesaUseCase createMesaUseCase;
    private final UpdateMesaUseCase updateMesaUseCase;
    private final GetMesaUseCase getMesaUseCase;
    private final ListMesasUseCase listMesasUseCase;
    private final DeleteMesaUseCase deleteMesaUseCase;
    private final RestoreMesaUseCase restoreMesaUseCase;
    private final ChangeMesaActiveUseCase changeMesaActiveUseCase;
    private final MesaRestMapper mapper = new MesaRestMapper();

    public MesasController(CreateMesaUseCase createMesaUseCase,
                           UpdateMesaUseCase updateMesaUseCase,
                           GetMesaUseCase getMesaUseCase,
                           ListMesasUseCase listMesasUseCase,
                           DeleteMesaUseCase deleteMesaUseCase,
                           RestoreMesaUseCase restoreMesaUseCase,
                           ChangeMesaActiveUseCase changeMesaActiveUseCase) {
        this.createMesaUseCase = createMesaUseCase;
        this.updateMesaUseCase = updateMesaUseCase;
        this.getMesaUseCase = getMesaUseCase;
        this.listMesasUseCase = listMesasUseCase;
        this.deleteMesaUseCase = deleteMesaUseCase;
        this.restoreMesaUseCase = restoreMesaUseCase;
        this.changeMesaActiveUseCase = changeMesaActiveUseCase;
    }

    @PostMapping
    public ResponseEntity<MesaResponse> create(@Valid @RequestBody MesaRequest request) {
        return ResponseEntity.ok(mapper.toResponse(createMesaUseCase.create(mapper.toDomain(request))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MesaResponse> update(@PathVariable("id") UUID id,
                                               @Valid @RequestBody MesaRequest request) {
        if (!isSuperadmin()) {
            Mesa existing = getMesaUseCase.getById(new MesaId(id));
            if (existing.isDeleted()) {
                return ResponseEntity.notFound().build();
            }
            request.setActivo(existing.isActive());
        }
        return ResponseEntity.ok(
                mapper.toResponse(updateMesaUseCase.update(new MesaId(id), mapper.toUpdateDomain(request)))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<MesaResponse> getById(@PathVariable("id") UUID id) {
        MesaResponse response = mapper.toResponse(getMesaUseCase.getById(new MesaId(id)));
        if (response.deleted() && !isSuperadmin()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<MesaResponse>> list(@RequestParam(value = "includeDeleted", required = false) Boolean includeDeleted) {
        boolean includeDeletedResolved = Boolean.TRUE.equals(includeDeleted) && isSuperadmin();
        List<MesaResponse> mesas = listMesasUseCase.list(includeDeletedResolved)
                .stream()
                .map(mapper::toResponse)
                .toList();
        return ResponseEntity.ok(mesas);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        deleteMesaUseCase.delete(new MesaId(id));
        return ResponseEntity.noContent().build();
    }

    /**
     * Restaura una mesa eliminada lógicamente.
     *
     * @param id identificador de la mesa.
     * @return respuesta con la mesa restaurada.
     */
    @PostMapping("/{id}/restore")
    public ResponseEntity<MesaResponse> restore(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(mapper.toResponse(restoreMesaUseCase.restore(new MesaId(id))));
    }

    @PostMapping("/{id}/active")
    @PreAuthorize("hasRole('SUPERADMINISTRADOR')")
    public ResponseEntity<MesaResponse> changeActive(@PathVariable("id") UUID id,
                                                     @Valid @RequestBody MesaActiveRequest request) {
        return ResponseEntity.ok(
                mapper.toResponse(changeMesaActiveUseCase.changeActive(new MesaId(id),
                        Boolean.TRUE.equals(request.getActivo())))
        );
    }

    private boolean isSuperadmin() {
        return "SUPERADMINISTRADOR".equals(SecurityUtils.currentRole());
    }
}
