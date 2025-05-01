package com.erpnext.services;

import com.erpnext.dto.ErpNextResponseDTO;
import com.erpnext.dto.SupplierQuotationDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ErpNextService {

    @Value("${erpnext.api.base-url}")
    private String baseUrl;

    @Value("${erpnext.api.api-key}")
    private String apiKey;

    @Value("${erpnext.api.api-secret}")
    private String apiSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<SupplierQuotationDTO> getQuotationsBySupplier(String supplier) {
        String url = baseUrl + "/Supplier Quotation" +
                "?filters=[[\"supplier\",\"=\",\"" + supplier + "\"]]" +
                "&fields=[\"name\",\"supplier\",\"transaction_date\",\"status\",\"grand_total\"]";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "token " + apiKey + ":" + apiSecret);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<ErpNextResponseDTO> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                ErpNextResponseDTO.class
        );

        ErpNextResponseDTO responseBody = response.getBody();
        return responseBody != null ? responseBody.getData() : List.of();
    }
}
