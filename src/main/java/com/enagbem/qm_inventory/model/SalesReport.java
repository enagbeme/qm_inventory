package com.enagbem.qm_inventory.model;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

public class SalesReport {
    private Long productId;
    private String productName;
    private String categoryName;
    private Long totalQuantitySold;
    private BigDecimal totalRevenue;
    private BigDecimal totalCost;
    private BigDecimal totalProfit;

    public SalesReport() {
    }

    public SalesReport(Long productId, String productName, String categoryName, Long totalQuantitySold,
                       BigDecimal totalRevenue, BigDecimal totalCost, BigDecimal totalProfit) {
        this.productId = productId;
        this.productName = productName;
        this.categoryName = categoryName;
        this.totalQuantitySold = totalQuantitySold;
        this.totalRevenue = totalRevenue;
        this.totalCost = totalCost;
        this.totalProfit = totalProfit;
    }

    public static SalesReportBuilder builder() {
        return new SalesReportBuilder();
    }

    public static class SalesReportBuilder {
        private final SalesReport salesReport = new SalesReport();

        public SalesReportBuilder productId(Long productId) {
            salesReport.setProductId(productId);
            return this;
        }

        public SalesReportBuilder productName(String productName) {
            salesReport.setProductName(productName);
            return this;
        }

        public SalesReportBuilder categoryName(String categoryName) {
            salesReport.setCategoryName(categoryName);
            return this;
        }

        public SalesReportBuilder totalQuantitySold(Long totalQuantitySold) {
            salesReport.setTotalQuantitySold(totalQuantitySold);
            return this;
        }

        public SalesReportBuilder totalRevenue(BigDecimal totalRevenue) {
            salesReport.setTotalRevenue(totalRevenue);
            return this;
        }

        public SalesReportBuilder totalCost(BigDecimal totalCost) {
            salesReport.setTotalCost(totalCost);
            return this;
        }

        public SalesReportBuilder totalProfit(BigDecimal totalProfit) {
            salesReport.setTotalProfit(totalProfit);
            return this;
        }

        public SalesReport build() {
            return salesReport;
        }
    }

    // Getters and Setters
    public Long getProductId() { return productId; }

    public void setProductId(Long productId) { this.productId = productId; }

    public String getProductName() { return productName; }

    public void setProductName(String productName) { this.productName = productName; }

    public String getCategoryName() { return categoryName; }

    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public Long getTotalQuantitySold() { return totalQuantitySold; }

    public void setTotalQuantitySold(Long totalQuantitySold) { this.totalQuantitySold = totalQuantitySold; }

    public BigDecimal getTotalRevenue() { return totalRevenue; }

    public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }

    public BigDecimal getTotalCost() { return totalCost; }

    public void setTotalCost(BigDecimal totalCost) { this.totalCost = totalCost; }

    public BigDecimal getTotalProfit() { return totalProfit; }

    public void setTotalProfit(BigDecimal totalProfit) { this.totalProfit = totalProfit; }
}
