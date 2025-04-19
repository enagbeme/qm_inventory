package com.enagbem.qm_inventory.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "demand_forecasts")
public class DemandForecast {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long forecastId;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "forecast_date", nullable = false)
    private LocalDate forecastDate;

    @Column(nullable = false)
    private String period;

    @Column(name = "predicted_quantity", nullable = false)
    private Double predictedQuantity;

    @Column(name = "confidence_score")
    private Double confidenceScore;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Constructors
    public DemandForecast() {}

    public DemandForecast(Long forecastId, Product product, LocalDate forecastDate, String period,
                          Double predictedQuantity, Double confidenceScore, LocalDateTime createdAt) {
        this.forecastId = forecastId;
        this.product = product;
        this.forecastDate = forecastDate;
        this.period = period;
        this.predictedQuantity = predictedQuantity;
        this.confidenceScore = confidenceScore;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getForecastId() {
        return forecastId;
    }

    public void setForecastId(Long forecastId) {
        this.forecastId = forecastId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public LocalDate getForecastDate() {
        return forecastDate;
    }

    public void setForecastDate(LocalDate forecastDate) {
        this.forecastDate = forecastDate;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Double getPredictedQuantity() {
        return predictedQuantity;
    }

    public void setPredictedQuantity(Double predictedQuantity) {
        this.predictedQuantity = predictedQuantity;
    }

    public Double getConfidenceScore() {
        return confidenceScore;
    }

    public void setConfidenceScore(Double confidenceScore) {
        this.confidenceScore = confidenceScore;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Builder
    public static class Builder {
        private Long forecastId;
        private Product product;
        private LocalDate forecastDate;
        private String period;
        private Double predictedQuantity;
        private Double confidenceScore;
        private LocalDateTime createdAt;

        public Builder forecastId(Long forecastId) {
            this.forecastId = forecastId;
            return this;
        }

        public Builder product(Product product) {
            this.product = product;
            return this;
        }

        public Builder forecastDate(LocalDate forecastDate) {
            this.forecastDate = forecastDate;
            return this;
        }

        public Builder period(String period) {
            this.period = period;
            return this;
        }

        public Builder predictedQuantity(Double predictedQuantity) {
            this.predictedQuantity = predictedQuantity;
            return this;
        }

        public Builder confidenceScore(Double confidenceScore) {
            this.confidenceScore = confidenceScore;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public DemandForecast build() {
            return new DemandForecast(forecastId, product, forecastDate, period,
                    predictedQuantity, confidenceScore, createdAt);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
