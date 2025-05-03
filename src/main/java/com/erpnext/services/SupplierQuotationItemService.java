package com.erpnext.services;

import com.erpnext.dto.SupplierQuotationItemListResponse;
import com.erpnext.dto.UpdateItemRateResponseDTO;
import com.erpnext.dto.UpdateItemRateResponseGroup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.servlet.http.HttpSession;

import java.util.Collections;

@Service
public class SupplierQuotationItemService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${erpnext.api.url}")
    private String erpnextApiUrl;

    @Value("${erpnext.api.api-key}")
    private String apiKey;

    @Value("${erpnext.api.api-secret}")
    private String apiSecret;
/*
    public SupplierQuotationItemListResponse getSupplierQuotation(HttpSession session, String quotationId) {
        String sid = (String) session.getAttribute("sid");
*//*        if (sid == null || sid.isEmpty()) {
            throw new RuntimeException("Session not authenticated");
        }*//*

        String url = String.format("%s/api/resource/Supplier Quotation/%s", erpnextApiUrl, quotationId);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Cookie", "sid=" + sid);

        HttpEntity<String> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<SupplierQuotationItemListResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    request,
                    SupplierQuotationItemListResponse.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();
            } else {
                throw new RuntimeException("Failed to fetch supplier quotation: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while fetching supplier quotation: " + e.getMessage(), e);
        }
    }*/

    public SupplierQuotationItemListResponse getSupplierQuotation(HttpSession session, String quotationId) {
        // Clés API à définir dans ton fichier de config ou injectées via @Value
                 // à remplacer

        String url = String.format("%s/api/resource/Supplier Quotation/%s", erpnextApiUrl, quotationId);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // Header d'authentification avec token
        String authToken = apiKey + ":" + apiSecret;
        headers.set("Authorization", "token " + authToken);

        HttpEntity<String> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<SupplierQuotationItemListResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    request,
                    SupplierQuotationItemListResponse.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();
            } else {
                throw new RuntimeException("Failed to fetch supplier quotation: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while fetching supplier quotation: " + e.getMessage(), e);
        }
    }





/*    public UpdateItemRateResponseDTO updateSupplierQuotationItemRate(HttpSession session, String itemName, double newRate) {
        String sid = (String) session.getAttribute("sid");
        if (sid == null || sid.isEmpty()) {
            throw new RuntimeException("Session not authenticated");
        }

        String apiUrl = String.format("%s/api/method/erpnext.eval.update_price.update_supplier_quotation_item_rate", erpnextApiUrl);

        
        @SuppressWarnings("deprecation")
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("item_name", itemName)
                .queryParam("new_rate", newRate);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Cookie", "sid=" + sid);

        HttpEntity<String> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<UpdateItemRateResponseGroup> response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.POST,
                    request,
                    UpdateItemRateResponseGroup.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody().getMessage();
            } else {
                return new UpdateItemRateResponseDTO("error", "Erreur HTTP: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la mise à jour du prix: " + e.getMessage(), e);
        }
    }*/

    public UpdateItemRateResponseDTO updateSupplierQuotationItemRate(HttpSession session, String itemName, double newRate) {
        String apiUrl = String.format("%s/api/method/erpnext.eval.update_price.update_supplier_quotation_item_rate", erpnextApiUrl);

        @SuppressWarnings("deprecation")
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("item_name", itemName)
                .queryParam("new_rate", newRate);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // Authentification avec apiKey et secretApi
        headers.set("Authorization", "token " + apiKey + ":" + apiSecret);

        HttpEntity<String> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<UpdateItemRateResponseGroup> response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.POST,
                    request,
                    UpdateItemRateResponseGroup.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody().getMessage();
            } else {
                return new UpdateItemRateResponseDTO("error", "Erreur HTTP: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la mise à jour du prix: " + e.getMessage(), e);
        }
    }

}
