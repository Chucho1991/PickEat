package com.pickeat.adapters.in.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ordenes")
public class OrdersController {
    @GetMapping
    public ResponseEntity<String> placeholder() {
        return ResponseEntity.ok("Coming soon");
    }
}
