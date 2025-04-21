package com.enagbem.qm_inventory.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class DemandForecastDTO {
    private LocalDate date;
    private Double forecastedQuantity; // used for forecast points
    private Double quantity;           // used for historical data
}
