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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Controlador REST para la administración de usuarios.
 */
@RestController
@RequestMapping("/users")
public class UsersController {
    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);
    private final CreateUserUseCase createUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final SoftDeleteUserUseCase softDeleteUserUseCase;
    private final RestoreUserUseCase restoreUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final ListUsersUseCase listUsersUseCase;
    private final GetUserUseCase getUserUseCase;
    private final GetMeUseCase getMeUseCase;
    private final UpdateMeUseCase updateMeUseCase;

    /**
     * Construye el controlador con los casos de uso requeridos.
     *
     * @param createUserUseCase caso de uso para creación de usuarios.
     * @param updateUserUseCase caso de uso para actualización de usuarios.
     * @param softDeleteUserUseCase caso de uso para eliminación lógica.
     * @param restoreUserUseCase caso de uso para restaurar usuarios.
     * @param deleteUserUseCase caso de uso para eliminación definitiva.
     * @param listUsersUseCase caso de uso para listar usuarios.
     * @param getUserUseCase caso de uso para obtener usuario por id.
     * @param getMeUseCase caso de uso para obtener perfil del usuario autenticado.
     * @param updateMeUseCase caso de uso para actualizar perfil del usuario autenticado.
     */
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

    /**
     * Crea un usuario nuevo.
     *
     * @param request datos de creación del usuario.
     * @return respuesta con el usuario creado.
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('SUPERADMINISTRADOR','ADMINISTRADOR')")
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserCreateRequest request) {
        logger.info("POST /users - creando usuario username={}, correo={}, rol={}, actor={}",
                request.getUsername(),
                request.getCorreo(),
                request.getRol(),
                SecurityUtils.currentUsername());
        User user = createUserUseCase.create(
                UserRestMapper.fromCreate(request),
                request.getPassword(),
                request.getConfirmPassword(),
                SecurityUtils.currentUsername(),
                SecurityUtils.currentRole()
        );
        return ResponseEntity.ok(UserRestMapper.toResponse(user));
    }

    /**
     * Lista usuarios según filtros y paginación.
     *
     * @param rol rol opcional para filtrar.
     * @param activo estado activo opcional para filtrar.
     * @param deleted estado eliminado opcional para filtrar.
     * @param pageable información de paginación.
     * @return página de usuarios.
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('SUPERADMINISTRADOR','ADMINISTRADOR')")
    public ResponseEntity<Page<UserResponse>> list(@RequestParam(required = false) String rol,
                                                   @RequestParam(required = false) Boolean activo,
                                                   @RequestParam(required = false) Boolean deleted,
                                                   Pageable pageable) {
        logger.info("GET /users - listando usuarios rol={}, activo={}, deleted={}, page={}, size={}",
                rol,
                activo,
                deleted,
                pageable.getPageNumber(),
                pageable.getPageSize());
        Role role = rol != null && !rol.isBlank() ? Role.from(rol) : null;
        Page<UserResponse> page = listUsersUseCase.list(role, activo, deleted, pageable)
                .map(UserRestMapper::toResponse);
        return ResponseEntity.ok(page);
    }

    /**
     * Obtiene un usuario por identificador.
     *
     * @param id identificador del usuario.
     * @return respuesta con el usuario encontrado.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPERADMINISTRADOR','ADMINISTRADOR')")
    public ResponseEntity<UserResponse> getById(@PathVariable UUID id) {
        logger.info("GET /users/{} - obteniendo usuario", id);
        return ResponseEntity.ok(UserRestMapper.toResponse(getUserUseCase.getById(id)));
    }

    /**
     * Actualiza los datos de un usuario.
     *
     * @param id identificador del usuario.
     * @param request datos de actualización.
     * @return respuesta con el usuario actualizado.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPERADMINISTRADOR','ADMINISTRADOR')")
    public ResponseEntity<UserResponse> update(@PathVariable UUID id,
                                               @Valid @RequestBody UserUpdateRequest request) {
        logger.info("PUT /users/{} - actualizando usuario username={}, correo={}, rol={}, activo={}, actor={}",
                id,
                request.getUsername(),
                request.getCorreo(),
                request.getRol(),
                request.getActivo(),
                SecurityUtils.currentUsername());
        User update = UserRestMapper.fromUpdate(request);
        User saved = updateUserUseCase.update(id, update, SecurityUtils.currentUsername(), SecurityUtils.currentRole());
        return ResponseEntity.ok(UserRestMapper.toResponse(saved));
    }

    /**
     * Marca un usuario como eliminado de forma lógica.
     *
     * @param id identificador del usuario.
     * @return respuesta con el usuario actualizado.
     */
    @PostMapping("/{id}/soft-delete")
    @PreAuthorize("hasAnyRole('SUPERADMINISTRADOR','ADMINISTRADOR')")
    public ResponseEntity<UserResponse> softDelete(@PathVariable UUID id) {
        logger.info("POST /users/{}/soft-delete - eliminando usuario actor={}", id, SecurityUtils.currentUsername());
        User saved = softDeleteUserUseCase.softDelete(id, SecurityUtils.currentUsername(), SecurityUtils.currentRole());
        return ResponseEntity.ok(UserRestMapper.toResponse(saved));
    }

    /**
     * Restaura un usuario eliminado lógicamente.
     *
     * @param id identificador del usuario.
     * @return respuesta con el usuario restaurado.
     */
    @PostMapping("/{id}/restore")
    @PreAuthorize("hasAnyRole('SUPERADMINISTRADOR','ADMINISTRADOR')")
    public ResponseEntity<UserResponse> restore(@PathVariable UUID id) {
        logger.info("POST /users/{}/restore - restaurando usuario actor={}", id, SecurityUtils.currentUsername());
        User saved = restoreUserUseCase.restore(id, SecurityUtils.currentUsername(), SecurityUtils.currentRole());
        return ResponseEntity.ok(UserRestMapper.toResponse(saved));
    }

    /**
     * Elimina un usuario de forma definitiva.
     *
     * @param id identificador del usuario.
     * @return respuesta sin contenido.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPERADMINISTRADOR')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        logger.info("DELETE /users/{} - eliminando usuario actor={}", id, SecurityUtils.currentUsername());
        deleteUserUseCase.delete(id, SecurityUtils.currentUsername(), SecurityUtils.currentRole());
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtiene el perfil del usuario autenticado.
     *
     * @return respuesta con el usuario autenticado.
     */
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe() {
        logger.info("GET /users/me - obteniendo perfil usuario={}", SecurityUtils.currentUsername());
        User user = getMeUseCase.getMe(SecurityUtils.currentUsername());
        return ResponseEntity.ok(UserRestMapper.toResponse(user));
    }

    /**
     * Actualiza el perfil del usuario autenticado.
     *
     * @param request datos de actualización del perfil.
     * @return respuesta con el usuario actualizado.
     */
    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateMe(@Valid @RequestBody UserMeUpdateRequest request) {
        logger.info("PUT /users/me - actualizando perfil usuario={}, username={}, correo={}, passwordProvided={}",
                SecurityUtils.currentUsername(),
                request.getUsername(),
                request.getCorreo(),
                request.getPassword() != null && !request.getPassword().isBlank());
        User current = getMeUseCase.getMe(SecurityUtils.currentUsername());
        User update = UserRestMapper.fromMeUpdate(request, current.getRol(), current.isActivo(), current.isDeleted());
        User saved = updateMeUseCase.updateMe(current.getUsername(), update, request.getPassword(), request.getConfirmPassword());
        return ResponseEntity.ok(UserRestMapper.toResponse(saved));
    }
}
