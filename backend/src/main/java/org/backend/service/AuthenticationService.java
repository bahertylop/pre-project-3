package org.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.backend.config.jwt.JwtService;
import org.backend.dto.CreateUserDto;
import org.backend.dto.request.LoginRequest;
import org.backend.dto.request.RefreshTokenRequest;
import org.backend.dto.request.SignUpRequest;
import org.backend.dto.response.JwtTokensResponse;
import org.backend.exception.InvalidOrExpiredRefreshTokenException;
import org.backend.model.Role;
import org.backend.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtService jwtService;

    private final UserService userService;

    private final UserDetailsService userDetailsService;

    private final AuthenticationManager authenticationManager;

    public JwtTokensResponse signUp(SignUpRequest request) {
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
        return jwtService.generateTokens(createdUser);
    }

    public JwtTokensResponse login(LoginRequest request) {
        log.info("sign in user with email: {}", request.getEmail());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        ));

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        log.info("user with email: {} signed in", request.getEmail());
        return jwtService.generateTokens(userDetails);
    }

    public JwtTokensResponse refreshTokens(RefreshTokenRequest request) {
        String token = request.getRefresh();
        String userEmail = jwtService.extractUserNameRefreshToken(token);

        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

        if (!jwtService.isRefreshTokenValid(token, userDetails)) {
            throw new InvalidOrExpiredRefreshTokenException("invalid or expired refresh token");
        }

        return jwtService.generateTokens(userDetails);
    }
}
