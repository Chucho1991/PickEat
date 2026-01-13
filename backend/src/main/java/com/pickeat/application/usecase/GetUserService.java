package com.pickeat.application.usecase;

import com.pickeat.domain.User;
import com.pickeat.ports.in.GetUserUseCase;
import com.pickeat.ports.out.UserRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetUserService implements GetUserUseCase {
    private final UserRepositoryPort userRepository;

    public GetUserService(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }
}
