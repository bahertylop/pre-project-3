package org.backend.exception;

public class IllegalRequestArgumentException extends IllegalArgumentException {

    public IllegalRequestArgumentException(String message) {
        super(message);
    }
}
