package com.edsuuu.list.handler;

import com.edsuuu.list.dto.ErrorResponseDTO;
import com.edsuuu.list.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFoundException(NotFoundException ex) {
        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .message(ex.getMessage())
                .statusCode(HttpStatus.NOT_FOUND.value())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}
