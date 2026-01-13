package com.pickeat.ports.in;

import com.pickeat.domain.User;

import java.util.UUID;

public interface UpdateUserUseCase {
    User update(UUID id, User update, String actor, String actorRole);
}
