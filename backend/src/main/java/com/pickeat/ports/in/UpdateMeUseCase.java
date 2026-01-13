package com.pickeat.ports.in;

import com.pickeat.domain.User;

public interface UpdateMeUseCase {
    User updateMe(String username, User update, String rawPassword, String confirmPassword);
}
