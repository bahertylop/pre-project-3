package org.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dto.response.ProfileResponse;
import org.backend.model.Role;
import org.backend.model.User;
import org.dto.request.UpdateProfileRequest;
import org.backend.dto.request.UpdateUserInfoRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserService userService;

    public ProfileResponse getUserInfo(UserDetails userDetails) {
        User user = (User) userDetails;
        log.info("get user info for user with chatId: {}", user.getChatId());
        return ProfileResponse.builder()
                .id(user.getId())
                .chatId(user.getChatId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .userName(user.getTgUserName())
                .roles(user.getRoles().stream().map(Role::getRole).map(Enum::toString).collect(Collectors.toSet()))
                .build();
    }

    public void deleteProfile(UserDetails userDetails) {
        User user = (User) userDetails;
        log.info("user delete account, chatId: {}", user.getChatId());
        userService.deleteUser(user.getId());
    }

    public void updateUserInfo(UserDetails userDetails, UpdateProfileRequest updateInfo) {
        User user = (User) userDetails;
        log.info("user update account, chatId: {}", user.getChatId());
        UpdateUserInfoRequest updateRequest = UpdateUserInfoRequest.builder()
                .id(user.getId())
                .firstName(updateInfo.getFirstName())
                .lastName(updateInfo.getLastName())
                .roles(user.getRoles().stream().map(Role::getRole).collect(Collectors.toSet()))
                .build();
        userService.updateUser(updateRequest);
    }
}
