package org.backend.exception;

public class CarPositionNotFoundException extends RuntimeException {

    public CarPositionNotFoundException(String message) {
        super(message);
    }
}
