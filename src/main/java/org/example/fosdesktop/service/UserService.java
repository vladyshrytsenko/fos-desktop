package org.example.fosdesktop.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.fosdesktop.model.dto.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    public UserDto getById(Long id) {
        HttpEntity<?> entity = new HttpEntity<>(createHeaders());

        ResponseEntity<UserDto> response = this.restTemplate.exchange(
            BASE_URL + "/" + id, HttpMethod.GET, entity, UserDto.class
        );
        return response.getBody();
    }

    public UserDto getByUsername(String username) {
        HttpEntity<?> entity = new HttpEntity<>(createHeaders());

        ResponseEntity<UserDto> response = this.restTemplate.exchange(
            BASE_URL + "/username/" + username, HttpMethod.GET, entity, UserDto.class
        );
        return response.getBody();
    }

    public UserDto getByEmail(String email) {
        HttpEntity<?> entity = new HttpEntity<>(createHeaders());

        ResponseEntity<UserDto> response = this.restTemplate.exchange(
            BASE_URL + "/email/" + email, HttpMethod.GET, entity, UserDto.class
        );
        return response.getBody();
    }

    public UserDto getByRole(String role) {
        HttpEntity<?> entity = new HttpEntity<>(createHeaders());

        ResponseEntity<UserDto> response = this.restTemplate.exchange(
            BASE_URL + "/role/" + role, HttpMethod.GET, entity, UserDto.class
        );
        return response.getBody();
    }

    public UserDto getCurrentUser() {
        HttpEntity<?> entity = new HttpEntity<>(createHeaders());

        ResponseEntity<UserDto> response = this.restTemplate.exchange(
            BASE_URL + "/current-user", HttpMethod.GET, entity, UserDto.class
        );
        UserDto body = response.getBody();
        return body;
    }

    public boolean isAdmin() {
        String token = this.storageService.getJwtToken();

        if (token != null) {
            Map<String, Object> payload = this.decodeToken(token);
            if (payload != null && payload.containsKey("role")) {
                if ("ADMIN".equals(payload.get("role"))) {
                    return true;
                }
            }
        }

        return false;
    }

    public List<UserDto> findAll() {
        HttpEntity<?> entity = new HttpEntity<>(createHeaders());

        ResponseEntity<List<UserDto>> response = this.restTemplate.exchange(
            BASE_URL, HttpMethod.GET, entity, new ParameterizedTypeReference<>() {}
        );
        return response.getBody() != null ? response.getBody() : Collections.emptyList();
    }

    public UserDto updateById(Long id, UserDto userDto) {
        HttpEntity<?> entity = new HttpEntity<>(userDto, createHeaders());

        ResponseEntity<UserDto> response = this.restTemplate.exchange(
            BASE_URL + "/" + id, HttpMethod.PUT, entity, UserDto.class
        );
        return response.getBody();
    }

    public void deleteById(Long id) {
        HttpEntity<?> entity = new HttpEntity<>(createHeaders());
        restTemplate.exchange(BASE_URL + "/" + id, HttpMethod.DELETE, entity, Void.class);
    }

    private HttpHeaders createHeaders() {
        String token = this.storageService.getJwtToken();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private Map<String, Object> decodeToken(String token) {
        try {
            String[] parts = token.split("\\.");
            String payload = parts[1];
            String decoded = new String(Base64.getDecoder().decode(payload));
            return this.objectMapper.readValue(decoded, new TypeReference<>() {});
        } catch (Exception e) {
            System.err.println("Error decoding token: " + e);
            return null;
        }
    }

    @Value("${user.resource}")
    private String BASE_URL;

    private final RestTemplate restTemplate;
    private final StorageService storageService;
    private final ObjectMapper objectMapper;
}
