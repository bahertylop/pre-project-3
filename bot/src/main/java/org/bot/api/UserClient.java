package org.bot.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bot.dto.SenderDto;
import org.bot.exception.ApiException;
import org.bot.exception.ForbiddenException;
import org.dto.response.ProfileResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserClient {

    private final RestTemplate restTemplate = new RestTemplate();

    private final ApiProperties apiProperties;

    public ProfileResponse getProfileInfo(SenderDto senderDto) {
        String apiUrlGetProfile = apiProperties.getUrl().getGetProfile();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(senderDto.getUser().getJwtToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<ProfileResponse> response = restTemplate.exchange(
                apiUrlGetProfile,
                HttpMethod.GET,
                entity,
                ProfileResponse.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.hasBody()) {
                return response.getBody();
            }

            log.warn("get profile info request returned status: {}, body: {}", response.getStatusCode(), response.getBody());
            throw new ApiException("api exception");
        } catch (HttpClientErrorException.Forbidden e) {
            log.error("get profile info request returned 403");
            throw new ForbiddenException("");
        } catch (RestClientException e) {
            log.warn("error with request get profile url: {}", apiUrlGetProfile, e);
            throw new ApiException("api exception");
        }
    }
}
