package com.erpnext.dto;

import lombok.Data;

@Data
public class InvoiceDTO {
    private String name;
    private String supplier;
    private String posting_date;
    private String status;
    private double grand_total;

}
