package org.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.backend.model.Role;


import javax.validation.constraints.*;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserInfoRequest {

    private Long id;

    @NotBlank(message = "Имя не должно быть пустым")
    private String name;

    @Size(min = 8, max = 50, message = "Пароль должен содержать от 8 до 50 символов")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$",
            message = "Пароль должен содержать хотя бы одну заглавную букву, одну строчную букву и одну цифру"
    )
    private String password;

    @NotNull(message = "Возраст не должен быть пустым")
    @Min(value = 12, message = "Минимальный возраст - 12 лет")
    private Integer age;

    @NotNull(message = "Не может не быть ролей")
    private Set<Role.ROLES> roles;
}
