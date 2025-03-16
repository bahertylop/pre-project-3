package org.backend.controller;

import lombok.RequiredArgsConstructor;
import org.backend.dto.CreateUserDto;
import org.backend.dto.UserDto;
import org.backend.dto.request.UpdateUserInfoRequest;
import org.backend.model.User;
import org.backend.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getUsers();
    }

    @PostMapping
    public UserDto createUser(@RequestBody @Validated CreateUserDto createUserDto) {
        User createdUser = userService.addNewUser(createUserDto);
        return UserDto.from(createdUser);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @PutMapping
    public UserDto updateUser(@RequestBody @Validated UpdateUserInfoRequest request) {
        return UserDto.from(userService.updateUser(request));
    }
}
