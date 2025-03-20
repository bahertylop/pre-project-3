package org.backend.exception;

public class CarPositionAlreadyExistsException extends RuntimeException {

    public CarPositionAlreadyExistsException(String message) {
        super(message);
    }
}
