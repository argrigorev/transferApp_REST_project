package ru.netology.springBootDemo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.netology.springBootDemo.exception.InvalidInputDataException;
import ru.netology.springBootDemo.exception.TransferProcessingException;

import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> handleInvalidInput(InvalidInputDataException e){
        return new ResponseEntity<>(Map.of(
                "message", e.getMessage(),
                "id", 1
        ), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> handleProcessingError(TransferProcessingException e){
        return new ResponseEntity<>(Map.of(
                "message", e.getMessage(),
                "id", 2
        ),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
