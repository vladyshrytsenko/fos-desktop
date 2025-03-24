package org.example.fosdesktop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ReportService {

    public byte[] generateReport() {
        HttpEntity<?> entity = new HttpEntity<>(createHeaders());
        ResponseEntity<byte[]> response = this.restTemplate.exchange(
            BASE_URL, HttpMethod.POST, entity, byte[].class
        );
        return response.getBody();
    }

    private HttpHeaders createHeaders() {
        String token = this.storageService.getJwtToken();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setAccept(MediaType.parseMediaTypes("application/octet-stream"));
        return headers;
    }

    @Value("${payment.resource}")
    private String BASE_URL;

    private final RestTemplate restTemplate;
    private final StorageService storageService;
}

