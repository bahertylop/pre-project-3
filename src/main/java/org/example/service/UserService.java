package org.example.service;

import org.example.dto.CreateUserDto;
import org.example.dto.UserDto;
import org.example.dto.request.UpdateUserInfoRequest;
import org.example.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserDto> getUsers();

    Optional<UserDto> getUserById(Long id);

    Optional<UserDto> getUserByEmail(String email);

    User addNewUser(CreateUserDto createUserDto);

    void deleteUser(Long id);

    User updateUser(UpdateUserInfoRequest request);
}
