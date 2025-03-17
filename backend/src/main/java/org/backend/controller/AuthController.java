package org.backend.controller;

import lombok.RequiredArgsConstructor;

import org.backend.dto.request.LoginRequest;
import org.backend.dto.request.RefreshTokenRequest;
import org.backend.dto.request.SignUpRequest;
import org.backend.dto.response.JwtTokensResponse;
import org.backend.service.AuthenticationService;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public JwtTokensResponse signUp(@RequestBody @Validated SignUpRequest request) {
        return authenticationService.signUp(request);
    }

    @PostMapping("/signin")
    public JwtTokensResponse signIn(@RequestBody @Validated LoginRequest request) {
        return authenticationService.login(request);
    }

    @PostMapping("/refresh")
    public JwtTokensResponse refresh(@RequestBody @Validated RefreshTokenRequest request) {
        return authenticationService.refreshTokens(request);
    }
}
