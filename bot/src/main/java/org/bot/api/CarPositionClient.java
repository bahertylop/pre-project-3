package org.bot.api;

import liquibase.pro.packaged.J;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.core.ApplicationPushBuilder;
import org.bot.dto.SenderDto;
import org.bot.exception.ApiException;
import org.bot.exception.ForbiddenException;
import org.dto.CarBrandDto;
import org.dto.CarModelDto;
import org.dto.CarPositionDto;
import org.dto.request.CreateCarPositionRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class CarPositionClient {

    @Value("${api.url.get-car-positions}")
    private String getCarPositionsApiUrl;

    @Value("${api.url.create-car-position}")
    private String createCarPositionApiUrl;

    @Value("${api.url.get-brands}")
    private String getCarBrandsApiUrl;

    @Value("${api.url.get-models}")
    private String getCarModelsApiUrl;

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

    public void addCarPosition(SenderDto sender, CreateCarPositionRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(sender.getUser().getJwtToken());

        HttpEntity<CreateCarPositionRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<Void> response = restTemplate.exchange(
                    createCarPositionApiUrl,
                    HttpMethod.POST,
                    entity,
                    Void.class
            );
        } catch (HttpClientErrorException.Forbidden e) {
            log.info("create car position returned 403");
            throw new ForbiddenException("create car position returned 403");
        }
        catch (RestClientException e) {
            log.error("create car position request failed url: {}", createCarPositionApiUrl, e);
            throw new ApiException("api unavailable", e);
        }
    }

    public List<CarBrandDto> getCarBrands() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<List<CarBrandDto>> response = restTemplate.exchange(
                    getCarBrandsApiUrl,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<CarBrandDto>>() {}
            );

            return response.getBody();
        } catch (RestClientException e) {
            log.error("get car brands request error: ", e);
            throw new ApiException("get car brands request error");
        }
    }

    public List<CarModelDto> getCarModels(CarBrandDto carBrand) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);

        Map<String, String> params = new HashMap<>();
        params.put("brandId", carBrand.getId().toString());

        String url = UriComponentsBuilder.fromHttpUrl(getCarModelsApiUrl).queryParam("brandId", "{brandId}").encode().toUriString();
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
            log.error("get car models request error: ", e);
            throw new ApiException("get car models request error");
        }
    }
}
