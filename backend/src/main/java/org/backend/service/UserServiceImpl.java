package org.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.backend.dto.CreateUserDto;
import org.backend.dto.UserDto;
import org.backend.exception.IllegalRequestArgumentException;
import org.backend.exception.UserAlreadyExistsException;
import org.backend.exception.UserNotFoundException;
import org.backend.model.User;
import org.backend.repository.RoleRepository;
import org.backend.repository.UserRepository;
import org.backend.dto.request.UpdateUserInfoRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    @Override
    public List<UserDto> getUsers() {
        return userRepository.findAll().stream().map(UserDto::from).collect(Collectors.toList());
    }

    @Override
    public Optional<UserDto> getUserById(Long id) {
        log.info("get user by id: {}", id);
        return userRepository.findById(id).map(UserDto::from);
    }

    @Override
    public Optional<UserDto> getUserByEmail(String email) {
        log.info("get user by email: {} ", email);
        return userRepository.getUserByEmail(email).map(UserDto::from);
    }

    @Override
    public User addNewUser(CreateUserDto createUserDto) {
        if (createUserDto == null || (createUserDto.getEmail() != null && userRepository.getUserByEmail(createUserDto.getEmail()).isPresent())) {
            log.error("try to add registered user, email: {}", createUserDto != null ? createUserDto.getEmail() : null);
            throw new UserAlreadyExistsException("Пользователь уже зарегистрирован");
        }
        log.info("add new user with email: {}", createUserDto.getEmail());

        return userRepository.save(User.builder()
                        .name(createUserDto.getName())
                        .email(createUserDto.getEmail())
                        .password(passwordEncoder.encode(createUserDto.getPassword()))
                        .age(createUserDto.getAge())
                        .roles(roleRepository.findByRoleIn(createUserDto.getRoles()))
                        .build());
    }


    @Override
    public void deleteUser(Long id) {
        log.info("delete user with id: {}", id);
        if (!userRepository.existsById(id)) {
            log.error("user with id {} not found", id);
            throw new UserNotFoundException("Пользователь с id: " + id + " не найден");
        }
        userRepository.deleteById(id);
    }

    @Override
    public User updateUser(UpdateUserInfoRequest request) {
        if (request == null || request.getId() == null) {
            log.error("not correct args, request || request.getId() is null, request: {}", request);
            throw new IllegalRequestArgumentException("Некорректные аргументы при обновлении пользователя");
        }
        log.info("update user with id: {}", request.getId());

        Optional<UserDto> userDtoOp = getUserById(request.getId());

        if (!userDtoOp.isPresent()) {
            log.error("user with id: {} not found", request.getId());
            throw new UserNotFoundException("Пользователь с id: " + request.getId() + "не найден");
        }

        User user = User.builder()
                .id(userDtoOp.get().getId())
                .name(request.getName())
                .email(userDtoOp.get().getEmail())
                .age(request.getAge())
                .roles(roleRepository.findByRoleIn(request.getRoles()))
                .build();
        if (request.getPassword() == null) {
            user.setPassword(userDtoOp.get().getPassword());
        } else {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        User updatedUser = userRepository.save(user);
        log.info("user with email: {} updated", updatedUser.getEmail());
        return updatedUser;
    }
}
