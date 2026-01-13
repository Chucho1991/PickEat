package com.pickeat.ports.in;

import com.pickeat.domain.User;

import java.util.UUID;

public interface GetUserUseCase {
    User getById(UUID id);
}
