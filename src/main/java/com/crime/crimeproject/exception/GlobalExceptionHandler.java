package com.crime.crimeproject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Обработчик исключений для ситуаций, когда аргументы метода не проходят валидацию
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodNotValidArgument(MethodArgumentNotValidException exception) {
        return ResponseEntity.badRequest().body(exception.getFieldErrors()
                .stream().map(it -> it.getField() + " " + it.getDefaultMessage())
                .collect(Collectors.toList()));
    }

    // Обработчик исключений для кастомного класса ValidationException
    @ExceptionHandler(value = ValidationException.class)
    public ResponseEntity<Object> handleValidationException(ValidationException exception) {
        return ResponseEntity.badRequest().body(List.of(exception.getMessage()));
    }

    // Обработчик исключений для NotFoundException, который обрабатывает случаи, когда нужный ресурс не найден
    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

}