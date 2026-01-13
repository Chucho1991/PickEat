package com.pickeat.application.usecase;

import com.pickeat.domain.Role;
import com.pickeat.domain.User;
import com.pickeat.ports.in.ListUsersUseCase;
import com.pickeat.ports.out.UserRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ListUsersService implements ListUsersUseCase {
    private final UserRepositoryPort userRepository;

    public ListUsersService(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Page<User> list(Role role, Boolean activo, Boolean deleted, Pageable pageable) {
        return userRepository.findAll(role, activo, deleted, pageable);
    }
}
