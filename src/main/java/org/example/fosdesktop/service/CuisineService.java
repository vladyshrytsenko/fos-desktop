package org.example.fosdesktop.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.fosdesktop.model.dto.CuisineDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CuisineService {

    private final ObjectMapper objectMapper;

    private final String BASE_URL = "http://localhost:8085/api/core/cuisines";

    public CuisineDto getById(Long id) {
        HttpEntity<?> entity = new HttpEntity<>(createAuthHeaders());
        ResponseEntity<CuisineDto> response = restTemplate.exchange(
            BASE_URL + "/" + id, HttpMethod.GET, entity, CuisineDto.class
        );
        return response.getBody();
    }

    public Map<String, Object> findAll() {
        HttpEntity<?> entity = new HttpEntity<>(createAuthHeaders());

        ResponseEntity<Map<String, Object>> response = this.restTemplate.exchange(
            BASE_URL, HttpMethod.GET, entity, new ParameterizedTypeReference<>() {}
        );

        return response.getBody() != null ? response.getBody() : Collections.emptyMap();
    }

    public CuisineDto create(CuisineDto cuisine) {
        HttpEntity<CuisineDto> entity = new HttpEntity<>(cuisine, createAuthHeaders());
        ResponseEntity<CuisineDto> response = restTemplate.exchange(
            BASE_URL, HttpMethod.POST, entity, CuisineDto.class
        );
        return response.getBody();
    }

    public CuisineDto updateById(Long id, CuisineDto cuisine) {
        HttpEntity<CuisineDto> entity = new HttpEntity<>(cuisine, createAuthHeaders());
        ResponseEntity<CuisineDto> response = restTemplate.exchange(
            BASE_URL + "/" + id, HttpMethod.PUT, entity, CuisineDto.class
        );
        return response.getBody();
    }

    public void deleteById(Long id) {
        HttpEntity<?> entity = new HttpEntity<>(createAuthHeaders());
        restTemplate.exchange(BASE_URL + "/" + id, HttpMethod.DELETE, entity, Void.class);
    }

    private HttpHeaders createAuthHeaders() {
        String token = this.storageService.getJwtToken();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        return headers;
    }

    private final RestTemplate restTemplate;
    private final StorageService storageService;
}
