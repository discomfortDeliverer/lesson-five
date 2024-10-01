package ru.discomfortdeliverer.lesson_five.exception.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.discomfortdeliverer.lesson_five.exception.CategoryNotFoundException;
import ru.discomfortdeliverer.lesson_five.exception.LocationNotFoundException;
import ru.discomfortdeliverer.lesson_five.exception.ReceivingDataFromExternalApiException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<String> handleCategoryNotFoundException(CategoryNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(LocationNotFoundException.class)
    public ResponseEntity<String> handleLocationNotFoundException(LocationNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(ReceivingDataFromExternalApiException.class)
    public ResponseEntity<String> handleReceivingDataFromExternalApiException(ReceivingDataFromExternalApiException e) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.getMessage());
    }
}
