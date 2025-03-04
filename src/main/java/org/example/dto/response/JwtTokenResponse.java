package org.example.dto.response;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtTokenResponse {

    @NotBlank(message = "jwt token не может быть пустым")
    String token;
}
