package com.pickeat.adapters.in.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones para controladores REST.
 */
@RestControllerAdvice
public class RestExceptionHandler {
    /**
     * Traduce errores de argumentos inválidos a una respuesta HTTP 400.
     *
     * @param ex excepción capturada.
     * @return respuesta con el detalle del error.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
    }

    /**
     * Traduce errores de validación de entrada a una respuesta HTTP 400.
     *
     * @param ex excepción con los errores de validación.
     * @return respuesta con los errores por campo.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(Map.of("errors", errors));
    }
}
