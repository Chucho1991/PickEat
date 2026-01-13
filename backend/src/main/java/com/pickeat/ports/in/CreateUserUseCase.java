package com.pickeat.ports.in;

import com.pickeat.domain.User;

public interface CreateUserUseCase {
    User create(User user, String rawPassword, String confirmPassword, String actor, String actorRole);
}
