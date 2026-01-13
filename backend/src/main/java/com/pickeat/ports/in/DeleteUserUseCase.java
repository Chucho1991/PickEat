package com.pickeat.ports.in;

import java.util.UUID;

public interface DeleteUserUseCase {
    void delete(UUID id, String actor, String actorRole);
}
