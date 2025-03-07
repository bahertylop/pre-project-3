package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.request.UpdateProfileRequest;
import org.example.dto.request.UpdateUserInfoRequest;
import org.example.dto.response.ProfileResponse;
import org.example.model.Role;
import org.example.model.User;
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
        log.info("get user info for user with email: {}", user.getEmail());
        return ProfileResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .age(user.getAge())
                .roles(user.getRoles().stream().map(Role::getRole).collect(Collectors.toSet()))
                .build();
    }

    public void deleteProfile(UserDetails userDetails) {
        User user = (User) userDetails;
        log.info("user delete account, email: {}", user.getEmail());
        userService.deleteUser(user.getId());
    }

    public void updateUserInfo(UserDetails userDetails, UpdateProfileRequest updateInfo) {
        User user = (User) userDetails;
        log.info("user update account, email: {}", user.getEmail());
        UpdateUserInfoRequest updateRequest = UpdateUserInfoRequest.builder()
                .id(user.getId())
                .name(updateInfo.getName())
                .password(updateInfo.getPassword())
                .age(updateInfo.getAge())
                .roles(user.getRoles().stream().map(Role::getRole).collect(Collectors.toSet()))
                .build();
        userService.updateUser(updateRequest);
    }
}
