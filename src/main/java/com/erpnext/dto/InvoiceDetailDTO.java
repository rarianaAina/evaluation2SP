package com.erpnext.dto;

import lombok.Data;

import java.util.List;

@Data
public class InvoiceDetailDTO {
    private String name;
    private String supplier;
    private String posting_date;
    private String due_date;
    private String status;
    private double grand_total;
    private List<InvoiceItemDTO> items;

    // Getters et setters
}