package org.example.fosdesktop.service;

import lombok.RequiredArgsConstructor;
import org.example.fosdesktop.model.dto.SubscriptionDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    public SubscriptionDto create(SubscriptionDto subscription) {
        HttpEntity<SubscriptionDto> entity = new HttpEntity<>(subscription, createHeaders());
        ResponseEntity<SubscriptionDto> response = restTemplate.exchange(
            BASE_URL, HttpMethod.POST, entity, SubscriptionDto.class
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

    private final String BASE_URL = "http://localhost:8085/api/core/subscriptions";

    private final RestTemplate restTemplate;
    private final StorageService storageService;
}
