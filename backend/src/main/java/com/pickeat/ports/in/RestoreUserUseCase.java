package com.pickeat.ports.in;

import com.pickeat.domain.User;

import java.util.UUID;

public interface RestoreUserUseCase {
    User restore(UUID id, String actor, String actorRole);
}
