package com.pickeat.application;

import com.pickeat.application.usecase.CreateUserService;
import com.pickeat.domain.Role;
import com.pickeat.domain.User;
import com.pickeat.ports.out.AuditRepositoryPort;
import com.pickeat.ports.out.UserRepositoryPort;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CreateUserServiceTest {
    @Test
    void createsUserWithHashedPassword() {
        UserRepositoryPort userRepository = Mockito.mock(UserRepositoryPort.class);
        AuditRepositoryPort auditRepository = Mockito.mock(AuditRepositoryPort.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        CreateUserService service = new CreateUserService(userRepository, auditRepository, passwordEncoder);

        User user = new User(UUID.randomUUID(), "Admin", "admin@pickeat.com", "admin", null, Role.ADMINISTRADOR,
                true, false, null, null, null, null);

        when(userRepository.existsByCorreo(user.getCorreo())).thenReturn(false);
        when(userRepository.existsByUsername(user.getUsername())).thenReturn(false);
        when(passwordEncoder.encode("secret")).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User saved = service.create(user, "secret", "secret", "system", "SUPERADMINISTRADOR");

        assertThat(saved.getPasswordHash()).isEqualTo("hashed");
    }
}
