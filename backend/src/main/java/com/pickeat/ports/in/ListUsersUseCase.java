package com.pickeat.ports.in;

import com.pickeat.domain.Role;
import com.pickeat.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ListUsersUseCase {
    Page<User> list(Role role, Boolean activo, Boolean deleted, Pageable pageable);
}
