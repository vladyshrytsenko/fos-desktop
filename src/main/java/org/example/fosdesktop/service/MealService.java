package org.example.fosdesktop.service;

import lombok.RequiredArgsConstructor;
import org.example.fosdesktop.model.dto.MealDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MealService {

    private final RestTemplate restTemplate;
    private final StorageService storageService;
    private final String baseUrl = "http://localhost:8085/api/core/meals";

    public MealDto getById(Long id) {
        HttpEntity<?> entity = new HttpEntity<>(createHeaders());
        ResponseEntity<MealDto> response = restTemplate.exchange(
            baseUrl + "/" + id, HttpMethod.GET, entity, MealDto.class
        );
        return response.getBody();
    }

    public MealDto getByName(String name) {
        HttpEntity<?> entity = new HttpEntity<>(createHeaders());
        ResponseEntity<MealDto> response = restTemplate.exchange(
            baseUrl + "/getBy?name=" + name, HttpMethod.GET, entity, MealDto.class
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

    public MealDto create(MealDto meal) {
        HttpEntity<MealDto> entity = new HttpEntity<>(meal, createHeaders());
        ResponseEntity<MealDto> response = restTemplate.exchange(
            baseUrl, HttpMethod.POST, entity, MealDto.class
        );
        return response.getBody();
    }

    public MealDto updateById(Long id, MealDto meal) {
        HttpEntity<MealDto> entity = new HttpEntity<>(meal, createHeaders());
        ResponseEntity<MealDto> response = restTemplate.exchange(
            baseUrl + "/" + id, HttpMethod.PUT, entity, MealDto.class
        );
        return response.getBody();
    }

    public void deleteById(Long id) {
        HttpEntity<?> entity = new HttpEntity<>(createHeaders());
        restTemplate.exchange(baseUrl + "/" + id, HttpMethod.DELETE, entity, Void.class);
    }

    private HttpHeaders createHeaders() {
        String token = this.storageService.getJwtToken();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
