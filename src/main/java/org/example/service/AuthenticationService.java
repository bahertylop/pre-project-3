package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.config.jwt.JwtService;
import org.example.dto.CreateUserDto;
import org.example.dto.request.LoginRequest;
import org.example.dto.request.SignUpRequest;
import org.example.dto.response.JwtTokenResponse;
import org.example.model.Role;
import org.example.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtService jwtService;

    private final UserService userService;

    private final UserDetailsService userDetailsService;

    private final AuthenticationManager authenticationManager;

    public JwtTokenResponse signUp(SignUpRequest request) {
        log.info("sign up user with email {}", request.getEmail());
        Set<Role.ROLES> roles = new HashSet<>();
        roles.add(Role.ROLES.ROLE_USER);

        CreateUserDto createUserDto = CreateUserDto.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .age(request.getAge())
                .roles(roles)
                .build();
        User createdUser = userService.addNewUser(createUserDto);
        log.info("user with email {} signed up", createdUser.getEmail());
        return new JwtTokenResponse(jwtService.generateToken(createdUser));
    }

    public JwtTokenResponse login(LoginRequest request) {
        log.info("sign in user with email: {}", request.getEmail());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        ));

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        log.info("user with email: {} signed in", request.getEmail());
        return new JwtTokenResponse(jwtService.generateToken(userDetails));
    }
}
