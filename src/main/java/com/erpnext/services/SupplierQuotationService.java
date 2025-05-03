package com.erpnext.services;

import com.erpnext.dto.ItemDTO;
import com.erpnext.dto.SupplierQuotationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Map;


@Service
public class SupplierQuotationService {

    @Value("${erpnext.api.url}")
    private String apiUrl;

    @Value("${erpnext.api.api-key}")
    private String apiKey;

    @Value("${erpnext.api.api-secret}")
    private String apiSecret;

    @Autowired
    private RestTemplate restTemplate;

    public SupplierQuotationDTO getSupplierQuotationDetails(String quotationName, String cookies) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Cookie", cookies);

            HttpEntity<String> entity = new HttpEntity<>("", headers);

            String url = apiUrl + "/api/resource/Supplier Quotation/" + quotationName;

            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> body = response.getBody();
                if (body != null && body.containsKey("data")) {
                    Map<String, Object> data = (Map<String, Object>) body.get("data");

                    SupplierQuotationDTO dto = new SupplierQuotationDTO();
                    dto.setName((String) data.get("name"));
                    dto.setSupplier((String) data.get("supplier"));
                    dto.setStatus((String) data.get("status"));
                    dto.setTransaction_date((String) data.get("transaction_date"));
                    // Mapping des items
                    List<Map<String, Object>> itemsData = (List<Map<String, Object>>) data.get("items");
                    List<ItemDTO> itemList = new ArrayList<>();

                    if (itemsData != null) {
                        for (Map<String, Object> item : itemsData) {
                            ItemDTO itemDTO = new ItemDTO();
                            itemDTO.setItem_code((String) item.get("item_code"));
                            itemDTO.setItem_name((String) item.get("item_name"));
                            itemDTO.setQty((Double) item.get("qty"));
                            itemDTO.setRate((Double) item.get("rate"));
                            itemDTO.setAmount((Double) item.get("amount"));

                            // Ajoutez d'autres champs ici si nécessaire
                            itemList.add(itemDTO);
                        }
                    }

                    dto.setItems(itemList);

                    return dto;
                }
            }
            return null;
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération du Supplier Quotation: " + e.getMessage());
            return null;
        }
    }

/*
    public boolean updateItemRate(String quotationName, String itemCode, double newRate, String cookies) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Cookie", cookies);

            // Construction du corps de la requête
            Map<String, Object> updateBody = Map.of(
                    "items", List.of(
                            Map.of(
                                    "item_code", itemCode,
                                    "rate", newRate
                            )
                    )
            );

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(updateBody, headers);

            String url = apiUrl + "/api/resource/Supplier Quotation/" + quotationName;

            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.PUT,
                    entity,
                    Map.class
            );

            return response.getStatusCode() == HttpStatus.OK;

        } catch (Exception e) {
            System.err.println("Erreur lors de la mise à jour du rate : " + e.getMessage());
            return false;
        }
    }

*/  public boolean updateItemRate(String quotationName, String itemCode, double newRate, String itemName, double qty, String uom) {
    try {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "token " + apiKey + ":" + apiSecret);

        // 1. Récupérer le Supplier Quotation existant
        String getUrl = apiUrl + "/api/resource/Supplier Quotation/" + quotationName;
        HttpEntity<Void> getEntity = new HttpEntity<>(headers);

        ResponseEntity<Map> getResponse = restTemplate.exchange(
                getUrl,
                HttpMethod.GET,
                getEntity,
                Map.class
        );

        Map<String, Object> data = (Map<String, Object>) getResponse.getBody().get("data");
        List<Map<String, Object>> items = (List<Map<String, Object>>) data.get("items");

        // 2. Modifier l'article concerné
        boolean found = false;
        System.out.println(uom);
        for (Map<String, Object> item : items) {
            // Vérifie si l'article correspond à celui qu'on veut mettre à jour
            if (itemCode.equals(item.get("item_code"))) {
                item.put("rate", newRate);
                item.put("base_rate", newRate);
                item.put("qty", qty);
                item.put("base_amount", qty * newRate);
                item.put("uom", "Are");
                item.put("stock_uom", "Nos");
                item.put("conversion_factor", 1);
            } else {
                // Pour tous les autres items, s'assurer que le champ 'uom' est bien présent
                if (!item.containsKey("uom")) {
                    item.put("uom", item.getOrDefault("stock_uom", "Nos")); // ou une valeur par défaut
                }
            }
        }


        if (!found) {
            System.err.println("Article " + itemCode + " non trouvé dans le Supplier Quotation " + quotationName);
            return false;
        }

        // 3. Mise à jour via PUT avec tous les items modifiés
        Map<String, Object> updateBody = Map.of("items", items);
        HttpEntity<Map<String, Object>> updateEntity = new HttpEntity<>(updateBody, headers);

        ResponseEntity<Map> updateResponse = restTemplate.exchange(
                getUrl,
                HttpMethod.PUT,
                updateEntity,
                Map.class
        );

        return updateResponse.getStatusCode() == HttpStatus.OK;

    } catch (Exception e) {
        System.err.println("Erreur lors de la mise à jour du rate : " + e.getMessage());
        e.printStackTrace();
        return false;
    }
}


}
