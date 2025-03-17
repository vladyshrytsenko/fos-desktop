package org.example.fosdesktop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PopularDishesService {

    private final RestTemplate restTemplate;
    private final StorageService storageService;
    private final String baseUrl = "http://localhost:8085/api/core/popular";

    public Map<String, String[]> findAll() {
        HttpEntity<?> entity = new HttpEntity<>(createHeaders());
        ResponseEntity<Map> response = restTemplate.exchange(
            baseUrl, HttpMethod.GET, entity, Map.class
        );
        return response.getBody();
    }

    private HttpHeaders createHeaders() {
        String token = this.storageService.getJwtToken();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}

