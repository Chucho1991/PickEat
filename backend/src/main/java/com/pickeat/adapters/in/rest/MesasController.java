package com.pickeat.adapters.in.rest;

import com.pickeat.adapters.in.rest.dto.MesaResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * Controlador REST para operaciones relacionadas con mesas.
 */
@RestController
@RequestMapping("/mesas")
public class MesasController {
    /**
     * Entrega el listado de mesas.
     *
     * @return listado de mesas.
     */
    @GetMapping
    public ResponseEntity<List<MesaResponse>> list() {
        List<MesaResponse> mesas = List.of(
                new MesaResponse(UUID.fromString("8b1a7774-2e3a-4ad3-8e1a-1a3d14a14a41"), "Terraza principal", 4, "ACTIVO"),
                new MesaResponse(UUID.fromString("2f3a7f12-3b06-4f87-8ef0-4f7d1c2f6d64"), "Salon norte", 6, "ACTIVO"),
                new MesaResponse(UUID.fromString("1e54093e-3385-48b8-9a60-c0a4baf0a0f1"), "Ventana", 2, "INACTIVO"),
                new MesaResponse(UUID.fromString("c6f9c08b-4b4f-4cf4-8de8-f0a5b02f16f4"), "Privado 1", 8, "ACTIVO"),
                new MesaResponse(UUID.fromString("96f6c1e7-8d5c-4d63-a1b5-6b0a3d3d3a1e"), "Barra", 3, "INACTIVO")
        );
        return ResponseEntity.ok(mesas);
    }
}
