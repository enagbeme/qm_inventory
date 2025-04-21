package com.enagbem.qm_inventory.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class Value {
    private LocalDate date;
    private Double value;
} 