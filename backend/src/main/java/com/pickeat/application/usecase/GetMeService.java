package com.pickeat.application.usecase;

import com.pickeat.domain.User;
import com.pickeat.ports.in.GetMeUseCase;
import com.pickeat.ports.out.UserRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class GetMeService implements GetMeUseCase {
    private final UserRepositoryPort userRepository;

    public GetMeService(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getMe(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }
}
