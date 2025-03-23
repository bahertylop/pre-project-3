package org.backend.controller;

import lombok.RequiredArgsConstructor;


import org.backend.service.AuthenticationService;

import org.dto.request.LoginRequest;
import org.dto.request.RefreshTokenRequest;
import org.dto.request.SignUpRequest;
import org.dto.request.TgAuthRequest;
import org.dto.response.JwtTokensResponse;
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

    @PostMapping("/tg_auth")
    public JwtTokensResponse tgAuth(@RequestBody @Validated TgAuthRequest tgAuthRequest) {
        return authenticationService.tgAuth(tgAuthRequest);
    }

//    @PostMapping("/signup")
//    public JwtTokensResponse signUp(@RequestBody @Validated SignUpRequest request) {
//        return authenticationService.signUp(request);
//    }

//    @PostMapping("/signin")
//    public JwtTokensResponse signIn(@RequestBody @Validated LoginRequest request) {
//        return authenticationService.login(request);
//    }

    @PostMapping("/refresh")
    public JwtTokensResponse refresh(@RequestBody @Validated RefreshTokenRequest request) {
        return authenticationService.refreshTokens(request);
    }
}
