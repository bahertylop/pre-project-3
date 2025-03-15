package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.CreateUserDto;
import org.example.dto.UserDto;
import org.example.dto.request.UpdateUserInfoRequest;
import org.example.model.User;
import org.example.service.UserService;
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
