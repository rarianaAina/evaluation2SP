package com.erpnext.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UpdateItemRateResponseGroup {
    @JsonProperty("message")
    UpdateItemRateResponseDTO message;
}
