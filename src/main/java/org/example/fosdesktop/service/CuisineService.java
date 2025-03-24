package org.example.fosdesktop.service;

import lombok.RequiredArgsConstructor;
import org.example.fosdesktop.model.dto.CuisineDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CuisineService {

    public CuisineDto getById(Long id) {
        HttpEntity<?> entity = new HttpEntity<>(createHeaders());
        ResponseEntity<CuisineDto> response = this.restTemplate.exchange(
            BASE_URL + "/" + id, HttpMethod.GET, entity, CuisineDto.class
        );
        return response.getBody();
    }

    public Map<String, Object> findAll() {
        HttpEntity<?> entity = new HttpEntity<>(createHeaders());

        ResponseEntity<Map<String, Object>> response = this.restTemplate.exchange(
            BASE_URL, HttpMethod.GET, entity, new ParameterizedTypeReference<>() {}
        );

        return response.getBody() != null ? response.getBody() : Collections.emptyMap();
    }

    public CuisineDto create(CuisineDto cuisine) {
        HttpEntity<CuisineDto> entity = new HttpEntity<>(cuisine, createHeaders());
        ResponseEntity<CuisineDto> response = this.restTemplate.exchange(
            BASE_URL, HttpMethod.POST, entity, CuisineDto.class
        );
        return response.getBody();
    }

    public CuisineDto updateById(Long id, CuisineDto cuisine) {
        HttpEntity<CuisineDto> entity = new HttpEntity<>(cuisine, createHeaders());
        ResponseEntity<CuisineDto> response = this.restTemplate.exchange(
            BASE_URL + "/" + id, HttpMethod.PUT, entity, CuisineDto.class
        );
        return response.getBody();
    }

    public void deleteById(Long id) {
        HttpEntity<?> entity = new HttpEntity<>(createHeaders());
        this.restTemplate.exchange(BASE_URL + "/" + id, HttpMethod.DELETE, entity, Void.class);
    }

    private HttpHeaders createHeaders() {
        String token = this.storageService.getJwtToken();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    @Value("${cuisine.resource}")
    private String BASE_URL;

    private final RestTemplate restTemplate;
    private final StorageService storageService;
}
