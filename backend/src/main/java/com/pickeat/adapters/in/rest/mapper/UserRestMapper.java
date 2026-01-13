package com.pickeat.adapters.in.rest.mapper;

import com.pickeat.adapters.in.rest.dto.UserCreateRequest;
import com.pickeat.adapters.in.rest.dto.UserMeUpdateRequest;
import com.pickeat.adapters.in.rest.dto.UserResponse;
import com.pickeat.adapters.in.rest.dto.UserUpdateRequest;
import com.pickeat.domain.Role;
import com.pickeat.domain.User;

public class UserRestMapper {
    public static User fromCreate(UserCreateRequest request) {
        return User.createNew(
                request.getNombres(),
                request.getCorreo(),
                request.getUsername(),
                null,
                Role.from(request.getRol())
        );
    }

    public static User fromUpdate(UserUpdateRequest request) {
        return new User(null,
                request.getNombres(),
                request.getCorreo(),
                request.getUsername(),
                null,
                Role.from(request.getRol()),
                request.getActivo(),
                false,
                null,
                null,
                null,
                null
        );
    }

    public static User fromMeUpdate(UserMeUpdateRequest request, Role role, boolean activo, boolean deleted) {
        return new User(null,
                request.getNombres(),
                request.getCorreo(),
                request.getUsername(),
                null,
                role,
                activo,
                deleted,
                null,
                null,
                null,
                null
        );
    }

    public static UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getNombres(),
                user.getCorreo(),
                user.getUsername(),
                user.getRol().name(),
                user.isActivo(),
                user.isDeleted(),
                user.getDeletedAt(),
                user.getDeletedBy(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
