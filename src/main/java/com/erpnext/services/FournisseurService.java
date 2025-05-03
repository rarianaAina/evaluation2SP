package com.erpnext.services;

import com.erpnext.dto.Fournisseur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;

@Service
public class FournisseurService {

    @Value("${erpnext.api.url}")
    private String apiUrl;

    @Autowired
    private RestTemplate restTemplate;

    public List<Fournisseur> getFournisseurs(String cookies) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Cookie", cookies);

            HttpEntity<String> entity = new HttpEntity<>("", headers);

            String url = apiUrl + "/api/resource/Supplier";

            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseData = response.getBody();

                if (responseData != null && responseData.containsKey("data")) {
                    List<Map<String, Object>> supplierList = (List<Map<String, Object>>) responseData.get("data");
                    List<Fournisseur> fournisseurs = new ArrayList<>();

                    for (Map<String, Object> supplier : supplierList) {
                        Fournisseur fournisseur = new Fournisseur();
                        fournisseur.setId((String) supplier.get("name"));
                        fournisseur.setName((String) supplier.get("name"));
                        fournisseur.setEmail((String) supplier.get("email"));
                        fournisseur.setTelephone((String) supplier.get("mobile_no"));
                        fournisseur.setStatut((String) supplier.get("status"));

                        fournisseurs.add(fournisseur);
                    }

                    return fournisseurs;
                }
            }

            return Collections.emptyList();
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des fournisseurs: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}