package com.pickeat.adapters.in.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para operaciones del despachador.
 */
@RestController
@RequestMapping("/despachador")
public class DispatcherController {
    /**
     * Entrega una respuesta provisional del módulo de despachador.
     *
     * @return mensaje indicando que el recurso está en construcción.
     */
    @GetMapping
    public ResponseEntity<String> placeholder() {
        return ResponseEntity.ok("Coming soon");
    }
}
