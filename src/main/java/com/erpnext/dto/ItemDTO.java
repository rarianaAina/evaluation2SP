package com.erpnext.dto;

import lombok.Data;

@Data
public class ItemDTO {
    private String item_code;
    private String item_name;
    private double qty;
    private double rate;
    private double amount;
    private String uom;
}