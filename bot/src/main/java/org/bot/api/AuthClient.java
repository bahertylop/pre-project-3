package org.bot.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bot.exception.AuthException;
import org.dto.request.LoginRequest;
import org.dto.request.RefreshTokenRequest;
import org.dto.response.JwtTokensResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


@Slf4j
@Component
@RequiredArgsConstructor
public class AuthClient {

    private final RestTemplate restTemplate = new RestTemplate();

    private final ApiProperties apiProperties;

    public JwtTokensResponse signIn(LoginRequest loginRequest) {
        String apiUrlSignIn = apiProperties.getUrl().getSignIn();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LoginRequest> request = new HttpEntity<>(loginRequest, headers);

        try {
            ResponseEntity<JwtTokensResponse> response = restTemplate.postForEntity(
                    apiUrlSignIn,
                    request,
                    JwtTokensResponse.class
            );

            if (response.getStatusCode().equals(HttpStatus.OK) && response.getBody() != null) {
                return response.getBody();
            }
            log.warn("sign-in request returned status: {}, body: {}", response.getStatusCode(), response.getBody());
            throw new AuthException("sign-in request returned " + response.getStatusCode(), response.getStatusCode());
        } catch (RestClientException e) {
            log.error("sign-in request failed url: {}", apiUrlSignIn, e);
            throw new AuthException("api is unavailable", HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    public JwtTokensResponse refreshTokens(RefreshTokenRequest refreshTokenRequest) {
        String apiUrlRefresh = apiProperties.getUrl().getRefresh();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<RefreshTokenRequest> request = new HttpEntity<>(refreshTokenRequest, headers);

        try {
            ResponseEntity<JwtTokensResponse> response = restTemplate.postForEntity(
                    apiUrlRefresh,
                    request,
                    JwtTokensResponse.class
            );

            if (response.getStatusCode().equals(HttpStatus.OK) && response.getBody() != null) {
                return response.getBody();
            }
            log.warn("refresh request returned status: {}, body: {}", response.getStatusCode(), response.getBody());
            throw new AuthException("refresh request returned " + response.getStatusCode(), response.getStatusCode());
        } catch (RestClientException e) {
            log.error("refresh request failed url: {}", apiUrlRefresh, e);
            throw new AuthException("api is unavailable", HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}
