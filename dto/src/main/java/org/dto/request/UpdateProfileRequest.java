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

    private String firstName;

    private String lastName;

    private String userName;
}
