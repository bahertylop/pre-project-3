package org.backend.exception;

public class CarBrandNotFoundException extends RuntimeException {

    public CarBrandNotFoundException(String message) {
        super(message);
    }
}
