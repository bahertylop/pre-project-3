package org.backend.dto;

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
public class CreateUserDto {

    private Long chatId;

    private String firstName;

    private String lastName;

    private String username;

    @NotNull(message = "не может не быть ролей")
    private Set<Role.ROLES> roles;
}
