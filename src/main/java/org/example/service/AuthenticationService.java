package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.config.jwt.JwtAuthenticationFilter;
import org.example.config.jwt.JwtService;
import org.example.dto.CreateUserDto;
import org.example.dto.UserDto;
import org.example.dto.request.LoginRequest;
import org.example.dto.request.SignUpRequest;
import org.example.dto.response.JwtTokenResponse;
import org.example.model.Role;
import org.example.model.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtService jwtService;

    private final UserService userService;

    private final UserDetailsService userDetailsService;

    private final AuthenticationManager authenticationManager;

    public JwtTokenResponse signUp(SignUpRequest request) {
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
        return new JwtTokenResponse(jwtService.generateToken(createdUser));

    }

    public JwtTokenResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        ));

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        return new JwtTokenResponse(jwtService.generateToken(userDetails));
    }
}
