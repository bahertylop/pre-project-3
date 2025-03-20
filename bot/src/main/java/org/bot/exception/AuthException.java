package org.bot.exception;

import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;

public class AuthException extends RuntimeException {

    @Getter
    private final HttpStatus status;

    public AuthException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
