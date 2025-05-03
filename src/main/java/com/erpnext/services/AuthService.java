package com.erpnext.services;

import com.erpnext.dto.Utilisateur;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;

@Service
public class AuthService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String LOGIN_URL = "http://127.0.0.1:8000/api/method/login";

    @Value("${erpnext.api.url}")
    private String apiUrl;

    public Utilisateur authentifier(String nom, String password) {
        try {
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("usr", nom);
            formData.add("pwd", password);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    apiUrl + "/api/method/login",
                    HttpMethod.POST,
                    requestEntity,
                    Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                List<String> cookies = response.getHeaders().get("Set-Cookie");
                String sessionCookies = String.join(";", cookies);

                HttpHeaders authHeaders = new HttpHeaders();
                authHeaders.add("Cookie", sessionCookies);

                HttpEntity<String> authEntity = new HttpEntity<>("", authHeaders);

                ResponseEntity<Map> userInfoResponse = restTemplate.exchange(
                        apiUrl + "/api/method/frappe.auth.get_logged_user",
                        HttpMethod.GET,
                        authEntity,
                        Map.class
                );

                Map<String, Object> userData = userInfoResponse.getBody();

                if (userData != null && userData.containsKey("message")) {
                    String username = (String) userData.get("message");

                    Utilisateur utilisateur = new Utilisateur();
                    utilisateur.setEmail(nom);
                    utilisateur.setNomComplet(username);
                    utilisateur.setCookies(sessionCookies);

                    return utilisateur;
                }
            }

            return null;
        } catch (Exception e) {
            System.err.println("Erreur d'authentification: " + e.getMessage());
            return null;
        }
    }
}
