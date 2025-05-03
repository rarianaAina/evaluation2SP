package com.erpnext.dto;

import lombok.Data;

@Data
public class PurchaseOrderDTO {
    private String name;
    private String supplier;
    private String transaction_date;
    private String status;
    private double grand_total;
}