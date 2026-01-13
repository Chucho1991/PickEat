package com.pickeat.adapters.in.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    @GetMapping
    @PreAuthorize("hasAnyRole('SUPERADMINISTRADOR','ADMINISTRADOR')")
    public ResponseEntity<Map<String, Object>> dashboard() {
        return ResponseEntity.ok(Map.of(
                "usuariosActivos", 0,
                "ordenesPendientes", 0,
                "mensaje", "Métricas en construcción"
        ));
    }
}
