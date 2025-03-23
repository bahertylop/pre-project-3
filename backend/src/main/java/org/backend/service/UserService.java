package org.backend.service;

import org.backend.dto.CreateUserDto;
import org.backend.dto.UserDto;
import org.backend.model.User;
import org.backend.dto.request.UpdateUserInfoRequest;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserDto> getUsers();

    Optional<UserDto> getUserById(Long id);

    Optional<UserDto> getUserByChatId(Long chatId);

    User addNewUser(CreateUserDto createUserDto);

    void deleteUser(Long id);

    User updateUser(UpdateUserInfoRequest request);
}
