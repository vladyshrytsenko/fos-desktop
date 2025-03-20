package org.example.fosdesktop.service;

import lombok.RequiredArgsConstructor;
import org.example.fosdesktop.model.dto.DrinkDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DrinkService {

    public DrinkDto getById(Long id) {
        HttpEntity<?> entity = new HttpEntity<>(createHeaders());
        ResponseEntity<DrinkDto> response = this.restTemplate.exchange(
            baseUrl + "/" + id, HttpMethod.GET, entity, DrinkDto.class
        );
        return response.getBody();
    }

    public DrinkDto getByName(String name) {
        HttpEntity<?> entity = new HttpEntity<>(createHeaders());
        ResponseEntity<DrinkDto> response = this.restTemplate.exchange(
            baseUrl + "/name/" + name, HttpMethod.GET, entity, DrinkDto.class
        );
        return response.getBody();
    }

    public Map<String, Object> findAll(int page, int size) {
        HttpEntity<?> entity = new HttpEntity<>(createHeaders());

        ResponseEntity<Map<String, Object>> response = this.restTemplate.exchange(
            baseUrl + "?page=" + page + "&size=" + size, HttpMethod.GET, entity, new ParameterizedTypeReference<>() {}
        );

        return response.getBody() != null ? response.getBody() : Collections.emptyMap();
    }

    public DrinkDto create(DrinkDto drink) {
        HttpEntity<DrinkDto> entity = new HttpEntity<>(drink, createHeaders());
        ResponseEntity<DrinkDto> response = this.restTemplate.exchange(
            baseUrl, HttpMethod.POST, entity, DrinkDto.class
        );
        return response.getBody();
    }

    public DrinkDto updateById(Long id, DrinkDto drink) {
        HttpEntity<DrinkDto> entity = new HttpEntity<>(drink, createHeaders());
        ResponseEntity<DrinkDto> response = this.restTemplate.exchange(
            baseUrl + "/" + id, HttpMethod.PUT, entity, DrinkDto.class
        );
        return response.getBody();
    }

    public void deleteById(Long id) {
        HttpEntity<?> entity = new HttpEntity<>(createHeaders());
        this.restTemplate.exchange(baseUrl + "/" + id, HttpMethod.DELETE, entity, Void.class);
    }

    public void deleteAllById(List<Long> ids) {
        HttpEntity<List<Long>> entity = new HttpEntity<>(ids, createHeaders());
        this.restTemplate.exchange(baseUrl + "/delete", HttpMethod.DELETE, entity, Void.class);
    }

    private HttpHeaders createHeaders() {
        String token = this.storageService.getJwtToken();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private final String baseUrl = "http://localhost:8085/api/core/drinks";

    private final RestTemplate restTemplate;
    private final StorageService storageService;
}
