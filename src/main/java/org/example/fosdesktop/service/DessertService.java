package org.example.fosdesktop.service;

import lombok.RequiredArgsConstructor;
import org.example.fosdesktop.model.dto.DessertDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DessertService {

    public DessertDto getById(Long id) {
        HttpEntity<?> entity = new HttpEntity<>(createHeaders());
        ResponseEntity<DessertDto> response = this.restTemplate.exchange(
            baseUrl + "/" + id, HttpMethod.GET, entity, DessertDto.class
        );
        return response.getBody();
    }

    public DessertDto getByName(String name) {
        HttpEntity<?> entity = new HttpEntity<>(createHeaders());
        ResponseEntity<DessertDto> response = this.restTemplate.exchange(
            baseUrl + "/getBy?name=" + name, HttpMethod.GET, entity, DessertDto.class
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

    public DessertDto create(DessertDto dessert) {
        HttpEntity<DessertDto> entity = new HttpEntity<>(dessert, createHeaders());
        ResponseEntity<DessertDto> response = this.restTemplate.exchange(
            baseUrl, HttpMethod.POST, entity, DessertDto.class
        );
        return response.getBody();
    }

    public DessertDto updateById(Long id, DessertDto dessert) {
        HttpEntity<DessertDto> entity = new HttpEntity<>(dessert, createHeaders());
        ResponseEntity<DessertDto> response = this.restTemplate.exchange(
            baseUrl + "/" + id, HttpMethod.PUT, entity, DessertDto.class
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

    @Value("${dessert.resource}")
    private String baseUrl;

    private final RestTemplate restTemplate;
    private final StorageService storageService;
}
