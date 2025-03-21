package org.example.fosdesktop.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import org.example.fosdesktop.model.dto.UserDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.awt.Desktop;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class AuthService {

    public void login() {
        String encodedRedirectUri = URLEncoder.encode(REDIRECT_URI, StandardCharsets.UTF_8);
        String authUrl =
            AUTH_SERVER_URL + "?response_type=code&client_id=" + CLIENT_ID + "&redirect_uri=" + encodedRedirectUri +
            "&scope=openid%20profile";

        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI(authUrl));
            } else {
                System.err.println("Desktop browsing not supported");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logout() {
        try {
            this.restTemplate.exchange(API_SERVER_URL + "/logout", HttpMethod.POST, null, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.storageService.removeJwtToken();
    }

    public boolean register(String username, String email, String password) {
        UserDto userDto = UserDto.builder()
            .username(username)
            .email(email)
            .password(password)
            .build();

        try {
            String requestBody = this.objectMapper.writeValueAsString(userDto);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = this.restTemplate.exchange(
                "http://localhost:8085/api/auth/users/auth/register", HttpMethod.POST, entity, String.class
            );

            return response.getStatusCode() == HttpStatus.CREATED;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static final String API_SERVER_URL = "http://localhost:8085";
    private static final String REDIRECT_URI = "http://localhost:8090/auth-callback";
    private static final String CLIENT_ID = "client";
    private static final String CLIENT_SECRET = "secret";
    private static final String AUTH_SERVER_URL = "http://localhost:8085/oauth2/authorize";

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final StorageService storageService;
}
