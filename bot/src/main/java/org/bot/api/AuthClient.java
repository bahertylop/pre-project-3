package org.bot.api;

import lombok.extern.slf4j.Slf4j;
import org.dto.request.LoginRequest;
import org.dto.response.JwtTokensResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.security.auth.message.AuthException;

@Slf4j
@Component
public class AuthClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${api.url.sign-in}")
    private String signInApiUrl;

    @Value("${api.url.refresh}")
    private String refreshApiUrl;

    public JwtTokensResponse signIn(LoginRequest loginRequest) throws AuthException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LoginRequest> request = new HttpEntity<>(loginRequest, headers);

        try {
            ResponseEntity<JwtTokensResponse> response = restTemplate.postForEntity(
                    signInApiUrl,
                    request,
                    JwtTokensResponse.class
            );

            if (response.getStatusCode().equals(HttpStatus.OK)) {
                return response.getBody();
            } else if (response.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
                log.warn("sign-in request returned 400");
                throw new AuthException("invalid sign-in request data");
            } else if (response.getStatusCode().equals(HttpStatus.FORBIDDEN)) {
                log.warn("sign-in request returned 403");
                throw new AuthException("invalid email or password");
            } else {
                log.warn("sign-in returned unexpected status: {}", response.getStatusCode());
                throw new AuthException("unexpected response status");
            }
        } catch (RestClientException e) {
            log.error("sign-in request failed", e);
            throw new AuthException("api is unavailable");
        }
    }
}
