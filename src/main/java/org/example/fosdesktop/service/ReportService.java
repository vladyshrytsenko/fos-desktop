package org.example.fosdesktop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final RestTemplate restTemplate;
    private final StorageService storageService;
    private final String baseUrl = "http://localhost:8085/api/core/reports";

    public byte[] generateReport() {
        HttpEntity<?> entity = new HttpEntity<>(createHeaders());
        ResponseEntity<byte[]> response = restTemplate.exchange(
            baseUrl, HttpMethod.POST, entity, byte[].class
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

//    byte[] report = reportService.generateReport();
//    Files.write(Path.of("report.pdf"), report);
}

