package com.pickeat.ports.in;

import com.pickeat.domain.User;

import java.util.UUID;

public interface SoftDeleteUserUseCase {
    User softDelete(UUID id, String actor, String actorRole);
}
