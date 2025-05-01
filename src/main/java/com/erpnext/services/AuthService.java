package com.erpnext.services;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service
public class AuthService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String LOGIN_URL = "http://127.0.0.1:8000/api/method/login";

    public String loginToErpNext(String username, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("usr", username);
        body.add("pwd", password);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(LOGIN_URL, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            // Récupérer le cookie de session
            String sessionCookie = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
            return sessionCookie;
        } else {
            throw new RuntimeException("Erreur lors de la connexion à ERPNext : " + response.getStatusCode());
        }
    }
}
