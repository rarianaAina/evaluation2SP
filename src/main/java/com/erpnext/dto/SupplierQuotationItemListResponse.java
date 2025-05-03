package com.erpnext.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SupplierQuotationItemListResponse {

    @JsonProperty("data")
    private SupplierQuotationGroupDto data;
}
