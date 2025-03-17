package org.backend.exception;

public class InvalidOrExpiredRefreshTokenException extends RuntimeException {

    public InvalidOrExpiredRefreshTokenException(String message) {
        super(message);
    }
}
