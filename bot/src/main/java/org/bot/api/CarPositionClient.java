package org.bot.api;


import lombok.extern.slf4j.Slf4j;
import org.bot.dto.SenderDto;
import org.bot.exception.ApiException;
import org.bot.exception.ForbiddenException;
import org.dto.CarPositionDto;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Component
public class CarPositionClient {

    @Value("${api.url.get-car-positions}")
    private String getCarPositionsApiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<CarPositionDto> getCarPositions(SenderDto senderDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(senderDto.getUser().getJwtToken());

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<List<CarPositionDto>> response = restTemplate.exchange(
                    getCarPositionsApiUrl,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<CarPositionDto>>() {}
            );

            if (response.getStatusCode().is2xxSuccessful() && response.hasBody()) {
                return response.getBody();
            }

            log.warn("get car positions request returned status: {}, body: {}", response.getStatusCode(), response.getBody());
            throw new ApiException("sign-in request returned" + response.getStatusCode());
        } catch (HttpClientErrorException.Forbidden e) {
            log.info("get car positions request returned 403 ");
            throw new ForbiddenException("");
        } catch (RestClientException e) {
            log.error("get car positions request failed url: {}", getCarPositionsApiUrl, e);
            throw new ApiException("api unavailable", e);
        }
    }
}
