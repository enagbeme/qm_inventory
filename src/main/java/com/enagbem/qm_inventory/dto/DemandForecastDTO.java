package com.enagbem.qm_inventory.dto;

import java.time.LocalDate;

public class DemandForecastDTO {
    private Long forecastId;
    private Long productId;
    private String productName;
    private LocalDate forecastDate;
    private String period;
    private Double predictedQuantity;
    private Double confidenceScore;

    public DemandForecastDTO() {}

    public DemandForecastDTO(Long forecastId, Long productId, String productName,
                             LocalDate forecastDate, String period,
                             Double predictedQuantity, Double confidenceScore) {
        this.forecastId = forecastId;
        this.productId = productId;
        this.productName = productName;
        this.forecastDate = forecastDate;
        this.period = period;
        this.predictedQuantity = predictedQuantity;
        this.confidenceScore = confidenceScore;
    }

    public Long getForecastId() { return forecastId; }
    public void setForecastId(Long forecastId) { this.forecastId = forecastId; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public LocalDate getForecastDate() { return forecastDate; }
    public void setForecastDate(LocalDate forecastDate) { this.forecastDate = forecastDate; }

    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }

    public Double getPredictedQuantity() { return predictedQuantity; }
    public void setPredictedQuantity(Double predictedQuantity) { this.predictedQuantity = predictedQuantity; }

    public Double getConfidenceScore() { return confidenceScore; }
    public void setConfidenceScore(Double confidenceScore) { this.confidenceScore = confidenceScore; }

    public static class Builder {
        private Long forecastId;
        private Long productId;
        private String productName;
        private LocalDate forecastDate;
        private String period;
        private Double predictedQuantity;
        private Double confidenceScore;

        public Builder forecastId(Long forecastId) { this.forecastId = forecastId; return this; }
        public Builder productId(Long productId) { this.productId = productId; return this; }
        public Builder productName(String productName) { this.productName = productName; return this; }
        public Builder forecastDate(LocalDate forecastDate) { this.forecastDate = forecastDate; return this; }
        public Builder period(String period) { this.period = period; return this; }
        public Builder predictedQuantity(Double predictedQuantity) { this.predictedQuantity = predictedQuantity; return this; }
        public Builder confidenceScore(Double confidenceScore) { this.confidenceScore = confidenceScore; return this; }

        public DemandForecastDTO build() {
            return new DemandForecastDTO(forecastId, productId, productName, forecastDate, period, predictedQuantity, confidenceScore);
        }
    }
}
