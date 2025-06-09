// Имя файла: GlobalExceptionHandler.java
// Расположение: src/main/java/com/example/kolos/exception/GlobalExceptionHandler.java
package com.example.kolos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

// @ControllerAdvice делает этот класс глобальным обработчиком исключений для всех контроллеров
@ControllerAdvice
public class GlobalExceptionHandler {

    // Обрабатывает исключения типа IllegalArgumentException, брошенные в любом слое
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {
        // Можно добавить логирование ex.getMessage() для отладки
        // logger.error("Validation error: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST); // Возвращаем 400 Bad Request с сообщением об ошибке
    }

    // Здесь можно добавить другие обработчики для других типов исключений,
    // например, ResourceNotFoundException (если сущность не найдена в базе)
    // @ExceptionHandler(ResourceNotFoundException.class)
    // public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
    //     return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND); // Возвращаем 404 Not Found
    // }

    // Можно добавить общий обработчик для всех необработанных исключений
    // @ExceptionHandler(Exception.class)
    // public ResponseEntity<Object> handleGlobalException(Exception ex, WebRequest request) {
    //     return new ResponseEntity<>("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); // Возвращаем 500 Internal Server Error
    // }
}