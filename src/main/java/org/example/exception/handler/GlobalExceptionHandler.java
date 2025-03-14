package org.example.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.ErrorDto;
import org.example.exception.IllegalRequestArgumentException;
import org.example.exception.UserAlreadyExistsException;
import org.example.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Пользователь уже зарегистрирован");
        errorResponse.put("message", ex.getMessage());
        log.error("error, user already exists", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFoundException(UserNotFoundException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Пользователь не был найден");
        errorResponse.put("message", ex.getMessage());
        log.error("error, user not found", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(IllegalRequestArgumentException.class)
    public ResponseEntity<Map<String, String>> handleRequestIllegalArgumentException(IllegalRequestArgumentException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Некорректный запрос");
        errorResponse.put("message", ex.getMessage());
        log.error("error, not legal args", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(org.springframework.validation.BindException.class)
    public ResponseEntity<Object> handleValidationExceptions(org.springframework.validation.BindException ex) {
        List<ErrorDto> errors = ex.getBindingResult()
                .getAllErrors().stream().map(ObjectError::getDefaultMessage).map(ErrorDto::new).collect(Collectors.toList());
        log.error("error, bind exception in request params", ex);
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}

