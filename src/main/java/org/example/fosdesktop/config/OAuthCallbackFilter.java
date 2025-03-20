package org.example.fosdesktop.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.example.fosdesktop.StartupApplication;
import org.example.fosdesktop.controller.MenuController;
import org.example.fosdesktop.service.AuthService;
import org.example.fosdesktop.service.CuisineService;
import org.example.fosdesktop.service.DessertService;
import org.example.fosdesktop.service.DrinkService;
import org.example.fosdesktop.service.MealService;
import org.example.fosdesktop.service.OrderService;
import org.example.fosdesktop.service.PaymentService;
import org.example.fosdesktop.service.StorageService;
import org.example.fosdesktop.service.UserService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

import static java.util.Objects.*;

@Component
@RequiredArgsConstructor
public class OAuthCallbackFilter implements Filter {

    @Override
    public void doFilter(
        ServletRequest request,
        ServletResponse response,
        FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        if (req.getRequestURI().equals("/auth-callback")) {
            String code = req.getParameter("code");
            if (code != null) {
                this.exchangeCodeForToken(code);
            }
            res.getWriter().write("Authentication successful! You can close this tab.");
            return;
        }

        chain.doFilter(request, response);
    }

    private void exchangeCodeForToken(String authCode) {
        String tokenUrl = API_SERVER_URL + "/oauth2/token";
        String requestBody = "grant_type=authorization_code&code=" + authCode
                             + "&client_id=" + CLIENT_ID
                             + "&redirect_uri=" + URLEncoder.encode(REDIRECT_URI, StandardCharsets.UTF_8);

        String credentials = CLIENT_ID + ":" + CLIENT_SECRET;
        String encodedCredentials = Base64.getEncoder().encodeToString(
            credentials.getBytes(StandardCharsets.UTF_8)
        );

        String accessToken = this.getAccessToken(encodedCredentials, requestBody, tokenUrl);
        this.storageService.setJwtToken(accessToken);
        this.loadMenuScene();
    }

    private String getAccessToken(
        String encodedCredentials,
        String requestBody,
        String tokenUrl) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Basic " + encodedCredentials);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map<String, Object>> response = this.restTemplate.exchange(
                tokenUrl, HttpMethod.POST, entity, new ParameterizedTypeReference<>() {}
            );
            Map<String, Object> responseBody = response.getBody();
            return requireNonNull(responseBody).get("access_token").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void loadMenuScene() {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/fxml/menu.fxml"));
                loader.setControllerFactory(param -> new MenuController(
                    this.drinkService,
                    this.mealService,
                    this.dessertService,
                    this.userService,
                    this.authService,
                    this.orderService,
                    this.cuisineService,
                    this.paymentService,
                    this.storageService,
                    this.objectMapper
                ));
                Parent root = loader.load();
                Scene menuScene = new Scene(root);

                Stage primaryStage = StartupApplication.getPrimaryStage();
                primaryStage.setTitle("FOS Desktop");
                primaryStage.setScene(menuScene);
                primaryStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private static final String API_SERVER_URL = "http://localhost:8085";
    private static final String CLIENT_ID = "client";
    private static final String CLIENT_SECRET = "secret";
    private static final String REDIRECT_URI = "http://localhost:8090/auth-callback";

    private final RestTemplate restTemplate;
    private final StorageService storageService;
    private final DrinkService drinkService;
    private final MealService mealService;
    private final DessertService dessertService;
    private final UserService userService;
    private final AuthService authService;
    private final OrderService orderService;
    private final CuisineService cuisineService;
    private final PaymentService paymentService;
    private final ObjectMapper objectMapper;
}
