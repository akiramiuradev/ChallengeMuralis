package com.miura.challenge_muralis.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


import java.util.Arrays;
import java.util.List;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleValidationException(ConstraintViolationException ex) {
        // Obter detalhes da exceção e retornar uma resposta adequada
        List<String> erros = Arrays.asList(ex.getMessage().split(","));
        return ResponseEntity.badRequest().body(erros);  // Retorna os erros com o status 400 Bad Request
    }

}