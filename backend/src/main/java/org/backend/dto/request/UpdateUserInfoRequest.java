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

    private String firstName;

    private String lastName;

    @NotNull(message = "Не может не быть ролей")
    private Set<Role.ROLES> roles;
}
