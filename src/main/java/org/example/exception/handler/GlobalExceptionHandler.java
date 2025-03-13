package org.example.exception.handler;

import liquibase.pro.packaged.O;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.ErrorDto;
import org.example.exception.*;
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

    @ExceptionHandler(CarPositionNotFoundException.class)
    public ResponseEntity<Object> handleCarPositionNotFoundException(CarPositionNotFoundException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Позиция не найдена");
        errorResponse.put("message", ex.getMessage());
        log.warn("user tried to get nonexistent position");
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CarPositionAccessDeniedException.class)
    public ResponseEntity<Object> handleCarPositionAccessDeniedException(CarPositionAccessDeniedException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Отказ в доступе");
        errorResponse.put("message", ex.getMessage());
        log.warn("user tried to get not his position");
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(CarPositionAlreadyExistsException.class)
    public ResponseEntity<Object> handleCarPositionAlreadyExistsException(CarPositionAlreadyExistsException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Позиция уже существует");
        errorResponse.put("message", ex.getMessage());
        log.warn("user tried to add already exists CarPosition");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CarBrandNotFoundException.class)
    public ResponseEntity<Object> handleCarBrandNotFoundException(CarBrandNotFoundException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Марка не найдена");
        errorResponse.put("message", ex.getMessage());
        log.warn("user tried to get nonexistent position");
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CarModelNotFoundException.class)
    public ResponseEntity<Object> handleCarModelNotFoundException(CarModelNotFoundException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Модель не найдена");
        errorResponse.put("message", ex.getMessage());
        log.warn("user tried to get nonexistent position");
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ParsingPricesException.class)
    public ResponseEntity<Object> handleParsingPricesException(ParsingPricesException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Parsing error");
        errorResponse.put("message", ex.getMessage());
        log.error("error with parsing avito", ex);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}

