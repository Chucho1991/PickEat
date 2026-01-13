package com.pickeat.adapters.in.rest;

import com.pickeat.adapters.in.rest.dto.UserCreateRequest;
import com.pickeat.adapters.in.rest.dto.UserMeUpdateRequest;
import com.pickeat.adapters.in.rest.dto.UserResponse;
import com.pickeat.adapters.in.rest.dto.UserUpdateRequest;
import com.pickeat.adapters.in.rest.mapper.UserRestMapper;
import com.pickeat.config.SecurityUtils;
import com.pickeat.domain.Role;
import com.pickeat.domain.User;
import com.pickeat.ports.in.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UsersController {
    private final CreateUserUseCase createUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final SoftDeleteUserUseCase softDeleteUserUseCase;
    private final RestoreUserUseCase restoreUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final ListUsersUseCase listUsersUseCase;
    private final GetUserUseCase getUserUseCase;
    private final GetMeUseCase getMeUseCase;
    private final UpdateMeUseCase updateMeUseCase;

    public UsersController(CreateUserUseCase createUserUseCase,
                           UpdateUserUseCase updateUserUseCase,
                           SoftDeleteUserUseCase softDeleteUserUseCase,
                           RestoreUserUseCase restoreUserUseCase,
                           DeleteUserUseCase deleteUserUseCase,
                           ListUsersUseCase listUsersUseCase,
                           GetUserUseCase getUserUseCase,
                           GetMeUseCase getMeUseCase,
                           UpdateMeUseCase updateMeUseCase) {
        this.createUserUseCase = createUserUseCase;
        this.updateUserUseCase = updateUserUseCase;
        this.softDeleteUserUseCase = softDeleteUserUseCase;
        this.restoreUserUseCase = restoreUserUseCase;
        this.deleteUserUseCase = deleteUserUseCase;
        this.listUsersUseCase = listUsersUseCase;
        this.getUserUseCase = getUserUseCase;
        this.getMeUseCase = getMeUseCase;
        this.updateMeUseCase = updateMeUseCase;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPERADMINISTRADOR','ADMINISTRADOR')")
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserCreateRequest request) {
        User user = createUserUseCase.create(
                UserRestMapper.fromCreate(request),
                request.getPassword(),
                request.getConfirmPassword(),
                SecurityUtils.currentUsername(),
                SecurityUtils.currentRole()
        );
        return ResponseEntity.ok(UserRestMapper.toResponse(user));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPERADMINISTRADOR','ADMINISTRADOR')")
    public ResponseEntity<Page<UserResponse>> list(@RequestParam(required = false) String rol,
                                                   @RequestParam(required = false) Boolean activo,
                                                   @RequestParam(required = false) Boolean deleted,
                                                   Pageable pageable) {
        Role role = rol != null ? Role.valueOf(rol) : null;
        Page<UserResponse> page = listUsersUseCase.list(role, activo, deleted, pageable)
                .map(UserRestMapper::toResponse);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPERADMINISTRADOR','ADMINISTRADOR')")
    public ResponseEntity<UserResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(UserRestMapper.toResponse(getUserUseCase.getById(id)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPERADMINISTRADOR','ADMINISTRADOR')")
    public ResponseEntity<UserResponse> update(@PathVariable UUID id,
                                               @Valid @RequestBody UserUpdateRequest request) {
        User update = UserRestMapper.fromUpdate(request);
        User saved = updateUserUseCase.update(id, update, SecurityUtils.currentUsername(), SecurityUtils.currentRole());
        return ResponseEntity.ok(UserRestMapper.toResponse(saved));
    }

    @PostMapping("/{id}/soft-delete")
    @PreAuthorize("hasAnyRole('SUPERADMINISTRADOR','ADMINISTRADOR')")
    public ResponseEntity<UserResponse> softDelete(@PathVariable UUID id) {
        User saved = softDeleteUserUseCase.softDelete(id, SecurityUtils.currentUsername(), SecurityUtils.currentRole());
        return ResponseEntity.ok(UserRestMapper.toResponse(saved));
    }

    @PostMapping("/{id}/restore")
    @PreAuthorize("hasAnyRole('SUPERADMINISTRADOR','ADMINISTRADOR')")
    public ResponseEntity<UserResponse> restore(@PathVariable UUID id) {
        User saved = restoreUserUseCase.restore(id, SecurityUtils.currentUsername(), SecurityUtils.currentRole());
        return ResponseEntity.ok(UserRestMapper.toResponse(saved));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPERADMINISTRADOR')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteUserUseCase.delete(id, SecurityUtils.currentUsername(), SecurityUtils.currentRole());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe() {
        User user = getMeUseCase.getMe(SecurityUtils.currentUsername());
        return ResponseEntity.ok(UserRestMapper.toResponse(user));
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateMe(@Valid @RequestBody UserMeUpdateRequest request) {
        User current = getMeUseCase.getMe(SecurityUtils.currentUsername());
        User update = UserRestMapper.fromMeUpdate(request, current.getRol(), current.isActivo(), current.isDeleted());
        User saved = updateMeUseCase.updateMe(current.getUsername(), update, request.getPassword(), request.getConfirmPassword());
        return ResponseEntity.ok(UserRestMapper.toResponse(saved));
    }
}
