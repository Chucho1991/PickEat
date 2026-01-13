package com.pickeat.adapters.in.rest;

import com.pickeat.adapters.in.rest.dto.AuthLoginRequest;
import com.pickeat.adapters.in.rest.dto.AuthResponse;
import com.pickeat.config.JwtService;
import com.pickeat.domain.User;
import com.pickeat.ports.in.LoginUseCase;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final LoginUseCase loginUseCase;
    private final JwtService jwtService;

    public AuthController(LoginUseCase loginUseCase, JwtService jwtService) {
        this.loginUseCase = loginUseCase;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthLoginRequest request) {
        logger.info("POST /auth/login - intento login usernameOrEmail={}", request.getUsernameOrEmail());
        User user = loginUseCase.login(request.getUsernameOrEmail(), request.getPassword());
        String token = jwtService.generateToken(user.getUsername(), Map.of(
                "role", user.getRol().name(),
                "userId", user.getId().toString()
        ));
        return ResponseEntity.ok(new AuthResponse(token, user.getId(), user.getNombres(), user.getUsername(), user.getRol().name()));
    }
}
