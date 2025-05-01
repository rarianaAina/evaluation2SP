package com.erpnext.dto;

import lombok.Data;
import java.util.List;

@Data
public class ErpNextResponseDTO<T> {
    private List<T> data;
}
