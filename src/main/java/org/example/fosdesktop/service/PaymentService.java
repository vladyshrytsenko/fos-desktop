package org.example.fosdesktop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {

    public boolean fetchPaymentIntent(double totalPrice) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("amount", totalPrice);
        requestBody.put("currency", "usd");

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, createHeaders());
        ResponseEntity<Map<String, Object>> response = this.restTemplate.exchange(
            BASE_URL, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<>() {}
        );

        return response.getStatusCode() == HttpStatus.OK;
    }

    private HttpHeaders createHeaders() {
        String token = this.storageService.getJwtToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private static final String BASE_URL = "http://localhost:8085/api/core/payments/create-payment-intent";

    private final StorageService storageService;
    private final RestTemplate restTemplate;
}
