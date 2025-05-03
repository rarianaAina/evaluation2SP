package com.erpnext.dto;

import lombok.Data;

@Data
public class InvoiceItemDTO {
    private String item_name;
    private int qty;
    private double rate;
    private double amount;

    // Getters et setters
}
