package org.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateProfileRequest {

    @NotBlank(message = "Имя не должно быть пустым")
    private String name;

    @NotBlank(message = "Пароль не должен быть пустым")
    @Size(min = 8, max = 50, message = "Пароль должен содержать от 8 до 50 символов")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$",
            message = "Пароль должен содержать хотя бы одну заглавную букву, одну строчную букву и одну цифру"
    )
    private String password;

    @NotNull(message = "Возраст не должен быть пустым")
    @Min(value = 12, message = "Минимальный возраст - 12 лет")
    private Integer age;
}
