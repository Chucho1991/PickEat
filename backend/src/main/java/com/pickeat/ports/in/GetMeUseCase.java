package com.pickeat.ports.in;

import com.pickeat.domain.User;

public interface GetMeUseCase {
    User getMe(String username);
}
