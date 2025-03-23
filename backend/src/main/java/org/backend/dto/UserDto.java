package org.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.backend.model.Role;
import org.backend.model.User;

import javax.validation.constraints.*;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private Long id;

    private Long chatId;

    private String firstName;

    private String lastName;

    private String userName;

    @NotNull(message = "Не может не быть ролей")
    private Set<Role.ROLES> roles;

    public static UserDto from(User user) {
        return UserDto.builder()
                .id(user.getId())
                .chatId(user.getChatId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .userName(user.getTgUserName())
                .roles(user.getRoles().stream().map(Role::getRole).collect(Collectors.toSet()))
                .build();
    }
}
