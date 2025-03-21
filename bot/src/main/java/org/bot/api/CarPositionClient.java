package org.bot.api;

import liquibase.pro.packaged.J;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.core.ApplicationPushBuilder;
import org.bot.dto.SenderDto;
import org.bot.exception.ApiException;
import org.bot.exception.ForbiddenException;
import org.dto.CarBrandDto;
import org.dto.CarModelDto;
import org.dto.CarPositionDto;
import org.dto.request.CreateCarPositionRequest;
import org.dto.response.CarPositionResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CarPositionClient {

    private final RestTemplate restTemplate = new RestTemplate();

    private final ApiProperties apiProperties;

    public List<CarPositionDto> getCarPositions(SenderDto senderDto) {
        String apiUrlGetCarPositions = apiProperties.getUrl().getGetCarPositions();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(senderDto.getUser().getJwtToken());

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<List<CarPositionDto>> response = restTemplate.exchange(
                    apiUrlGetCarPositions,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<CarPositionDto>>() {}
            );

            if (response.getStatusCode().is2xxSuccessful() && response.hasBody()) {
                return response.getBody();
            }

            log.warn("get car positions request returned status: {}, body: {}", response.getStatusCode(), response.getBody());
            throw new ApiException("get car positions request returned" + response.getStatusCode());
        } catch (HttpClientErrorException.Forbidden e) {
            log.info("get car positions request returned 403 ");
            throw new ForbiddenException("");
        } catch (RestClientException e) {
            log.error("get car positions request failed url: {}", apiUrlGetCarPositions, e);
            throw new ApiException("api unavailable", e);
        }
    }

    public CarPositionResponse getCarPosition(SenderDto sender, Long carPositionId) {
        String apiUrlGetCarPosition = apiProperties.getUrl().getGetCarPosition();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(sender.getUser().getJwtToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<CarPositionResponse> response = restTemplate.exchange(
                    apiUrlGetCarPosition + carPositionId,
                    HttpMethod.GET,
                    entity,
                    CarPositionResponse.class
            );

            if (response.getStatusCode().is2xxSuccessful() || response.hasBody()) {
                return response.getBody();
            }

            log.warn("get car positions request id: {} returned status: {}, body: {}", carPositionId, response.getStatusCode(), response.getBody());
            throw new ApiException("get car position request returned" + response.getStatusCode());
        } catch (HttpClientErrorException.Forbidden e) {
            log.info("get car position id: {} request returned 403", carPositionId);
            throw new ForbiddenException("get car position request returned 403");
        } catch (RestClientException e) {
            log.error("get car position id: {} request failed url: {}", carPositionId, apiUrlGetCarPosition, e);
            throw new ApiException("api unavailable", e);
        }
    }

    public void addCarPosition(SenderDto sender, CreateCarPositionRequest request) {
        String apiUrlCreateCarPosition = apiProperties.getUrl().getCreateCarPosition();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(sender.getUser().getJwtToken());

        HttpEntity<CreateCarPositionRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<Void> response = restTemplate.exchange(
                    apiUrlCreateCarPosition,
                    HttpMethod.POST,
                    entity,
                    Void.class
            );
        } catch (HttpClientErrorException.Forbidden e) {
            log.info("create car position returned 403");
            throw new ForbiddenException("create car position returned 403");
        }
        catch (RestClientException e) {
            log.error("create car position request failed url: {}", apiUrlCreateCarPosition, e);
            throw new ApiException("api unavailable", e);
        }
    }

    public List<CarBrandDto> getCarBrands() {
        String apiUrlGetBrands = apiProperties.getUrl().getGetBrands();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<List<CarBrandDto>> response = restTemplate.exchange(
                    apiUrlGetBrands,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<CarBrandDto>>() {}
            );

            return response.getBody();
        } catch (RestClientException e) {
            log.error("get car brands request error url: {}", apiUrlGetBrands, e);
            throw new ApiException("get car brands request error");
        }
    }

    public List<CarModelDto> getCarModels(CarBrandDto carBrand) {
        String apiUrlGetModels = apiProperties.getUrl().getGetModels();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);

        Map<String, String> params = new HashMap<>();
        params.put("brandId", carBrand.getId().toString());

        String url = UriComponentsBuilder.fromHttpUrl(apiUrlGetModels).queryParam("brandId", "{brandId}").encode().toUriString();
        try {
            ResponseEntity<List<CarModelDto>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<CarModelDto>>() {},
                    params
            );

            return response.getBody();
        } catch (RestClientException e) {
            log.error("get car models request error url: {}", url, e);
            throw new ApiException("get car models request error");
        }
    }
}
