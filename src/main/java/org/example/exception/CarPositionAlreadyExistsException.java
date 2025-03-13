package org.example.exception;

public class CarPositionAlreadyExistsException extends RuntimeException {

    public CarPositionAlreadyExistsException(String message) {
        super(message);
    }
}
