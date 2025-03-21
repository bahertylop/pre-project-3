package org.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileResponse {

    private Long id;

    private String name;

    private String email;

    private Integer age;

    private Set<String> roles;
}
