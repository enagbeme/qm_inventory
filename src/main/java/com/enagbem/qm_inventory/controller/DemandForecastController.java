package com.enagbem.qm_inventory.controller;

import com.enagbem.qm_inventory.dto.DemandForecastDTO;
import com.enagbem.qm_inventory.service.DemandForecastService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/demand-forecast")
@RequiredArgsConstructor
public class DemandForecastController {

    private final DemandForecastService demandForecastService;

    @GetMapping("/{productId}")
    public Map<String, List<DemandForecastDTO>> getDemandForecast(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "30") int days) {
        return demandForecastService.getDemandForecast(productId, days);
    }
}
