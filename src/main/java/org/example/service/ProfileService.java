package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.UserDto;
import org.example.dto.request.UpdateProfileRequest;
import org.example.dto.request.UpdateUserInfoRequest;
import org.example.dto.response.ProfileResponse;
import org.example.exception.IllegalRequestArgumentException;
import org.example.exception.UserNotFoundException;
import org.example.model.Role;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserService userService;

    public ProfileResponse getUserInfo(UserDetails userDetails) {
        User user = (User) userDetails;

        return ProfileResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .age(user.getAge())
                .roles(user.getRoles().stream().map(Role::getRole).collect(Collectors.toSet()))
                .build();
//
//        if (email == null) {
//            throw new IllegalRequestArgumentException("email не может быть null");
//        }
//
//        Optional<UserDto> userDtoOp = userService.getUserByEmail(email);
//        if (!userDtoOp.isPresent()) {
//            throw new UserNotFoundException("пользователь с email: " + email + " не найден");
//        }
//
//        UserDto userDto = userDtoOp.get();
//        return ProfileResponse.builder()
//                .id(userDto.getId())
//                .name(userDto.getName())
//                .email(userDto.getEmail())
//                .age(userDto.getAge())
//                .roles(userDto.getRoles())
//                .build();
    }

    public void deleteProfile(UserDetails userDetails) {
        User user = (User) userDetails;
        userService.deleteUser(user.getId());
    }

    public void updateUserInfo(UserDetails userDetails, UpdateProfileRequest updateInfo) {
        User user = (User) userDetails;
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
