package org.bot.util;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class PasswordValidator {

    private final Pattern PASSWORD_VALIDATION_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$");

    public boolean isValid(String password) {
        return password != null && PASSWORD_VALIDATION_PATTERN.matcher(password).matches();
    }
}
