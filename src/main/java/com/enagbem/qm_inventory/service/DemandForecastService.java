package com.enagbem.qm_inventory.service;

import com.enagbem.qm_inventory.dto.DemandForecastDTO;
import com.enagbem.qm_inventory.dto.Value;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DemandForecastService {

    private final RestTemplate restTemplate;

    @org.springframework.beans.factory.annotation.Value("${ml.service.url:http://localhost:5000}")
    private String mlServiceUrl;

    public Map<String, List<DemandForecastDTO>> getDemandForecast(Long productId, int days) {
        try {
            Map<String, Object> requestBody = Map.of(
                    "product_id", productId,
                    "days", days
            );

            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.postForObject(
                    mlServiceUrl + "/predict-demand",
                    requestBody,
                    Map.class
            );

            if (response == null) {
                throw new RuntimeException("Empty response from forecasting service");
            }

            List<DemandForecastDTO> forecast = ((List<Map<String, Object>>) response.get("forecast")).stream()
                    .map(item -> {
                        DemandForecastDTO dto = new DemandForecastDTO();
                        dto.setDate(LocalDate.parse((String) item.get("date"), DateTimeFormatter.ISO_DATE));
                        dto.setForecastedQuantity(((Number) item.get("forecastedQuantity")).doubleValue());
                        return dto;
                    })
                    .toList();

            List<DemandForecastDTO> historical = ((List<Map<String, Object>>) response.get("historical")).stream()
                    .map(item -> {
                        DemandForecastDTO dto = new DemandForecastDTO();
                        dto.setDate(LocalDate.parse((String) item.get("date"), DateTimeFormatter.ISO_DATE));
                        dto.setQuantity(((Number) item.get("quantity")).doubleValue()); // ✅ this is key!
                        return dto;
                    })
                    .toList();

            return Map.of("forecast", forecast, "historical", historical);

        } catch (Exception e) {
            log.error("Error calling forecasting service", e);
            throw new RuntimeException("Forecasting error: " + e.getMessage());
        }
    }

} 