package com.erpnext.dto;

import lombok.Data;

@Data
public class SupplierQuotationDTO {
    private String name;
    private String supplier;
    private String transaction_date;
    private String status;
    private Double grand_total;
}
