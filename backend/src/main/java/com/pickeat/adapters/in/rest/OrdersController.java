package com.pickeat.adapters.in.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para operaciones relacionadas con órdenes.
 */
@RestController
@RequestMapping("/ordenes")
public class OrdersController {
    /**
     * Entrega una respuesta provisional del módulo de órdenes.
     *
     * @return mensaje indicando que el recurso está en construcción.
     */
    @GetMapping
    public ResponseEntity<String> placeholder() {
        return ResponseEntity.ok("Coming soon");
    }
}
