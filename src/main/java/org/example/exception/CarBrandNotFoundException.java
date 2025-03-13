package org.example.exception;

public class CarBrandNotFoundException extends RuntimeException {

    public CarBrandNotFoundException(String message) {
        super(message);
    }
}
