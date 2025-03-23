package org.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.backend.config.jwt.JwtService;
import org.backend.dto.CreateUserDto;
import org.backend.dto.UserDto;
import org.backend.exception.InvalidOrExpiredRefreshTokenException;
import org.backend.model.Role;
import org.backend.model.User;
import org.dto.request.LoginRequest;
import org.dto.request.RefreshTokenRequest;
import org.dto.request.SignUpRequest;
import org.dto.request.TgAuthRequest;
import org.dto.response.JwtTokensResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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

    public JwtTokensResponse tgAuth(TgAuthRequest request) {
        Optional<UserDto> userDto = userService.getUserByChatId(request.getTelegramId());

        if (userDto.isEmpty()) {
            return signUp(request);
        }

        return login(request);
    }

    public JwtTokensResponse signUp(TgAuthRequest request) {
        log.info("sign up user with chatId {}", request.getTelegramId());
        Set<Role.ROLES> roles = new HashSet<>();
        roles.add(Role.ROLES.ROLE_USER);

        CreateUserDto createUserDto = CreateUserDto.builder()
                .chatId(request.getTelegramId())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .username(request.getUserName())
                .roles(roles)
                .build();
        User createdUser = userService.addNewUser(createUserDto);
        log.info("user with chatId {} signed up", createdUser.getChatId());
        return jwtService.generateTokens(createdUser);
    }

    public JwtTokensResponse login(TgAuthRequest request) {
        log.info("sign in user with chatId: {}", request.getTelegramId());

        UserDetails userDetails = userDetailsService.loadUserByUsername(String.valueOf(request.getTelegramId()));
        log.info("user with chatId: {} signed in", request.getTelegramId());
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
