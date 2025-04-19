package com.enagbem.qm_inventory.service;


import com.enagbem.qm_inventory.dto.DemandForecastDTO;
import com.enagbem.qm_inventory.exception.ResourceNotFoundException;
import com.enagbem.qm_inventory.model.DemandForecast;
import com.enagbem.qm_inventory.model.Product;
import com.enagbem.qm_inventory.repository.DemandForecastRepository;
import com.enagbem.qm_inventory.repository.OrderRepository;
import com.enagbem.qm_inventory.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DemandForecastingService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final DemandForecastRepository forecastRepository;

    public DemandForecastingService(OrderRepository orderRepository, ProductRepository productRepository, DemandForecastRepository forecastRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.forecastRepository = forecastRepository;
    }

    @Transactional
    public DemandForecastDTO generateDemandForecast(Long productId, String period, int numPeriods) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        // Get historical sales data (last 6 months)
        LocalDateTime startDate = LocalDateTime.now().minusMonths(6);
        List<Object[]> salesData = orderRepository.findProductSalesHistory(productId, startDate);

        if (salesData.isEmpty()) {
            throw new IllegalStateException("Insufficient sales data for forecasting");
        }

        // Prepare data for linear regression
        SimpleRegression regression = new SimpleRegression();
        for (int i = 0; i < salesData.size(); i++) {
            Object[] row = salesData.get(i);
            double periodNum = i;
            double quantity = ((Number) row[1]).doubleValue();
            regression.addData(periodNum, quantity);
        }

        // Predict demand for next period
        double prediction = regression.predict(salesData.size() + numPeriods);
        double confidence = calculateConfidence(regression);

        // Save forecast
        DemandForecast forecast = DemandForecast.builder()
                .product(product)
                .forecastDate(LocalDate.now())
                .period(period)
                .predictedQuantity(prediction)
                .confidenceScore(confidence)
                .build();

        DemandForecast savedForecast = forecastRepository.save(forecast);
        return convertToDTO(savedForecast);
    }

    @Transactional(readOnly = true)
    public List<DemandForecastDTO> getProductForecasts(Long productId) {
        return forecastRepository.findByProduct_ProductId(productId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DemandForecastDTO> getRecentForecasts(int days) {
        LocalDate cutoffDate = LocalDate.now().minusDays(days);
        return forecastRepository.findByForecastDateAfter(cutoffDate).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private double calculateConfidence(SimpleRegression regression) {
        // Simple confidence calculation based on R-squared
        double rSquared = regression.getRSquare();
        return Math.min(1.0, Math.max(0.0, rSquared * 0.9)); // Scale down slightly
    }

    private DemandForecastDTO convertToDTO(DemandForecast forecast) {
        return new DemandForecastDTO.Builder()
                .forecastId(forecast.getForecastId())
                .productId(forecast.getProduct().getProductId())
                .productName(forecast.getProduct().getName())
                .forecastDate(forecast.getForecastDate())
                .period(forecast.getPeriod())
                .predictedQuantity(forecast.getPredictedQuantity())
                .confidenceScore(forecast.getConfidenceScore())
                .build();
    }
}
