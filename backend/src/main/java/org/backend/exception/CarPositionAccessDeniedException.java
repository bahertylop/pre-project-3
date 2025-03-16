package org.backend.exception;

public class CarPositionAccessDeniedException extends RuntimeException {
    public CarPositionAccessDeniedException(String s) {
        super(s);
    }
}
