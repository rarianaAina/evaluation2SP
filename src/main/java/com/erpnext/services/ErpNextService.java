package com.erpnext.services;

import com.erpnext.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ErpNextService {

    @Value("${erpnext.api.base-url}")
    private String baseUrl;

    @Value("${erpnext.api.api-key}")
    private String apiKey;

    @Value("${erpnext.api.api-secret}")
    private String apiSecret;

    @Value("http://127.0.0.1:8000")
    private String apiUrl;


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

    public List<PurchaseOrderDTO> getOrdersBySupplier(String supplier) {
        String url = baseUrl + "/Purchase Order" +
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

    public List<InvoiceDTO> getAllInvoices() {
        String url = baseUrl + "/Purchase Invoice?fields=[\"name\",\"supplier\",\"posting_date\",\"status\",\"grand_total\"]";

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

        return response.getBody() != null ? response.getBody().getData() : List.of();
    }

    public InvoiceDetailDTO getInvoiceDetails(String invoiceName) {
        String url = baseUrl + "/Purchase Invoice/" + invoiceName;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "token " + apiKey + ":" + apiSecret);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<InvoiceDetailDTO> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                InvoiceDetailDTO.class
        );

        return response.getBody();
    }

    public boolean mettreAJourPrix(String cookies, String devisId, List<DemandeDevis.ArticleDevis> articles) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Cookie", cookies);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> requestData = new HashMap<>();
            requestData.put("items", articles.stream().map(article -> {
                Map<String, Object> item = new HashMap<>();
                item.put("name", article.getId());
                item.put("rate", article.getPrix());
                return item;
            }).toList());

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestData, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    apiUrl + "/api/resource/Supplier Quotation/" + devisId,
                    HttpMethod.PUT,
                    entity,
                    Map.class
            );

            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            System.err.println("Erreur lors de la mise à jour des prix: " + e.getMessage());
            return false;
        }
    }

    public String payInvoice(String invoiceName, double amount, String supplier, String modeOfPayment, String paidToAccount, float source_exchange_rate) {
        String url = baseUrl + "/Payment Entry";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "token " + apiKey + ":" + apiSecret);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Création de l'objet de paiement
        Map<String, Object> paymentData = new HashMap<>();
        paymentData.put("payment_type", "Pay");
        paymentData.put("party_type", "Supplier");
        paymentData.put("party", supplier);
        paymentData.put("paid_amount", amount);
        paymentData.put("received_amount", amount);
        paymentData.put("mode_of_payment", modeOfPayment);
        paymentData.put("paid_to", paidToAccount);
        paymentData.put("reference_no", "INV-" + invoiceName); // optionnel
        paymentData.put("reference_date", LocalDate.now().toString());
        paymentData.put("source_exchange_rate", source_exchange_rate > 0 ? source_exchange_rate : 1.0);
        paymentData.put("source_currency", "EUR");  // ou autre devise
        paymentData.put("target_currency", "EUR");  // ou celle de paid_to
        paymentData.put("paid_from", "Bank Account - RD");  // Exemple : "Cash - MG"
        paymentData.put("paid_from_account_currency", "EUR");    // Exemple : "MGA" ou autre

        // Lien avec la facture (Purchase Invoice)
        Map<String, Object> reference = new HashMap<>();
        reference.put("reference_doctype", "Purchase Invoice");
        reference.put("reference_name", invoiceName);
        reference.put("total_amount", amount);
        reference.put("outstanding_amount", amount);
        reference.put("allocated_amount", amount);

        paymentData.put("references", List.of(reference));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(paymentData, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        return response.getBody();
    }

}
