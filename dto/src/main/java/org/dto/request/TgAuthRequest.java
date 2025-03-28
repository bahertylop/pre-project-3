package org.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TgAuthRequest {

    private Long telegramId;

    private String firstName;

    private String lastName;

    private String username;
}
