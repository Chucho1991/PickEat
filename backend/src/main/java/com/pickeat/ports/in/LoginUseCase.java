package com.pickeat.ports.in;

import com.pickeat.domain.User;

public interface LoginUseCase {
    User login(String usernameOrEmail, String password);
}
