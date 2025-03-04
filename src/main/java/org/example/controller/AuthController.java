package org.example.controller;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.example.dto.request.LoginRequest;
import org.example.dto.request.SignUpRequest;
import org.example.dto.response.JwtTokenResponse;
import org.example.service.AuthenticationService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public JwtTokenResponse signUp(@RequestBody @Validated SignUpRequest request) {
         return authenticationService.signUp(request);
    }

    @PostMapping("/signin")
    public JwtTokenResponse signIn(@RequestBody @Validated LoginRequest request) {
        return authenticationService.login(request);
    }
}
