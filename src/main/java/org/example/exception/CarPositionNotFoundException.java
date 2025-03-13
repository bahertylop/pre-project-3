package org.example.exception;

public class CarPositionNotFoundException extends RuntimeException {

    public CarPositionNotFoundException(String message) {
        super(message);
    }
}
