package com.pickeat.application.usecase;

import com.pickeat.domain.ActionAuditType;
import com.pickeat.domain.User;
import com.pickeat.ports.in.LoginUseCase;
import com.pickeat.ports.out.AuditRepositoryPort;
import com.pickeat.ports.out.UserRepositoryPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService implements LoginUseCase {
    private final UserRepositoryPort userRepository;
    private final AuditRepositoryPort auditRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginService(UserRepositoryPort userRepository,
                        AuditRepositoryPort auditRepository,
                        PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.auditRepository = auditRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User login(String usernameOrEmail, String password) {
        User user = userRepository.findByUsernameOrCorreo(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas"));
        if (user.isDeleted() || !user.isActivo()) {
            throw new IllegalArgumentException("Usuario inactivo o eliminado");
        }
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }
        auditRepository.saveActionAudit(ActionAuditType.LOGIN, "AUTH", user.getUsername(),
                user.getRol().name(), user.getId().toString(), "users");
        return user;
    }
}
