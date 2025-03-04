package org.example.util;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
public @interface ValidPassword {
    String message() default "Пароль должен содержать хотя бы одну заглавную букву, одну строчную букву и одну цифру";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
