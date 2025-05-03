package com.erpnext.dto;

import lombok.Data;
import java.util.List;

@Data
public class SupplierQuotationDTO {
    private String name;
    private String supplier;
    private String transaction_date;
    private String creation;
    private String status;
    private List<ItemDTO> items;


}
