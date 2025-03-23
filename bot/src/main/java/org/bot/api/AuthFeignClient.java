package org.bot.api;

import org.dto.request.LoginRequest;
import org.dto.request.RefreshTokenRequest;
import org.dto.response.JwtTokensResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.awt.*;

@FeignClient(name = "authClient", url = "${api.url.base-url}")
public interface AuthFeignClient {

    @PostMapping(value = "${api.url.sign-in}", consumes = MediaType.APPLICATION_JSON_VALUE)
    JwtTokensResponse signInUser(@RequestBody LoginRequest loginRequest);

    @PostMapping(value = "${api.url.refresh}", consumes = MediaType.APPLICATION_JSON_VALUE)
    JwtTokensResponse refreshTokens(@RequestBody RefreshTokenRequest refreshTokenRequest);
}
