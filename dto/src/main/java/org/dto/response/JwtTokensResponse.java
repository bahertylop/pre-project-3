package org.dto.response;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtTokensResponse {

    @NotBlank(message = "access токен не может быть пустым")
    String access;

    @NotBlank(message = "refresh токен не может быть пустым")
    String refresh;
}
