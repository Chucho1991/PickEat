package com.pickeat.adapters.in.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Controlador REST para exponer el estado de salud de la API.
 */
@RestController
public class HealthController {
    /**
     * Verifica el estado básico de la API.
     *
     * @return respuesta con el estado "ok".
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "ok"));
    }
}
