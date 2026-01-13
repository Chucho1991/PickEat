package com.pickeat.application;

import com.pickeat.application.usecase.LoginService;
import com.pickeat.domain.Role;
import com.pickeat.domain.User;
import com.pickeat.ports.out.AuditRepositoryPort;
import com.pickeat.ports.out.UserRepositoryPort;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class LoginServiceTest {
    @Test
    void loginReturnsUser() {
        UserRepositoryPort userRepository = Mockito.mock(UserRepositoryPort.class);
        AuditRepositoryPort auditRepository = Mockito.mock(AuditRepositoryPort.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        LoginService service = new LoginService(userRepository, auditRepository, passwordEncoder);

        User user = new User(UUID.randomUUID(), "Admin", "admin@pickeat.com", "admin", "hashed", Role.ADMINISTRADOR,
                true, false, null, null, null, null);

        when(userRepository.findByUsernameOrCorreo("admin", "admin")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("secret", "hashed")).thenReturn(true);

        User result = service.login("admin", "secret");

        assertThat(result.getUsername()).isEqualTo("admin");
    }
}
