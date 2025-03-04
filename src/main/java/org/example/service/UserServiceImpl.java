package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.CreateUserDto;
import org.example.dto.UserDto;
import org.example.dto.request.UpdateUserInfoRequest;
import org.example.exception.IllegalRequestArgumentException;
import org.example.exception.UserAlreadyExistsException;
import org.example.exception.UserNotFoundException;
import org.example.model.User;
import org.example.repository.RoleRepository;
import org.example.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        return userRepository.findById(id).map(UserDto::from);
    }

    @Override
    public Optional<UserDto> getUserByEmail(String email) {
        return userRepository.getUserByEmail(email).map(UserDto::from);
    }

    @Override
    public User addNewUser(CreateUserDto createUserDto) {
        if (createUserDto == null || (createUserDto.getEmail() != null && userRepository.getUserByEmail(createUserDto.getEmail()).isPresent())) {
            throw new UserAlreadyExistsException("Пользователь уже зарегистрирован");
        }

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
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("Пользователь с id: " + id + " не найден");
        }
        userRepository.deleteById(id);
    }

    @Override
    public User updateUser(UpdateUserInfoRequest request) {
        if (request == null || request.getId() == null) {
            throw new IllegalRequestArgumentException("Некорректные аргументы при обновлении пользователя");
        }
        Optional<UserDto> userDtoOp = getUserById(request.getId());

        if (!userDtoOp.isPresent()) {
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

        return userRepository.save(user);
    }
}
