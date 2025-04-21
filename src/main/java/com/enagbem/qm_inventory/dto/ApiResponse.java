package com.enagbem.qm_inventory.dto;

import lombok.Data;
import java.util.List;

@Data
public class ApiResponse {
    private List<Value> values;
}

