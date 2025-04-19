package com.enagbem.qm_inventory.dto;



import lombok.Data;
import java.math.BigDecimal;

@Data
public class SalesReportDTO {
    private Long productId;
    private String productName;
    private String categoryName;
    private Long totalQuantitySold;
    private BigDecimal totalRevenue;
    private BigDecimal totalCost;
    private BigDecimal totalProfit;

    public SalesReportDTO() {
    }

    public SalesReportDTO(Long productId, String productName, String categoryName, Long totalQuantitySold,
                          BigDecimal totalRevenue, BigDecimal totalCost, BigDecimal totalProfit) {
        this.productId = productId;
        this.productName = productName;
        this.categoryName = categoryName;
        this.totalQuantitySold = totalQuantitySold;
        this.totalRevenue = totalRevenue;
        this.totalCost = totalCost;
        this.totalProfit = totalProfit;
    }

    public static SalesReportDTOBuilder builder() {
        return new SalesReportDTOBuilder();
    }

    public static class SalesReportDTOBuilder {
        private final SalesReportDTO salesReportDTO = new SalesReportDTO();

        public SalesReportDTOBuilder productId(Long productId) {
            salesReportDTO.setProductId(productId);
            return this;
        }

        public SalesReportDTOBuilder productName(String productName) {
            salesReportDTO.setProductName(productName);
            return this;
        }

        public SalesReportDTOBuilder categoryName(String categoryName) {
            salesReportDTO.setCategoryName(categoryName);
            return this;
        }

        public SalesReportDTOBuilder totalQuantitySold(Long totalQuantitySold) {
            salesReportDTO.setTotalQuantitySold(totalQuantitySold);
            return this;
        }

        public SalesReportDTOBuilder totalRevenue(BigDecimal totalRevenue) {
            salesReportDTO.setTotalRevenue(totalRevenue);
            return this;
        }

        public SalesReportDTOBuilder totalCost(BigDecimal totalCost) {
            salesReportDTO.setTotalCost(totalCost);
            return this;
        }

        public SalesReportDTOBuilder totalProfit(BigDecimal totalProfit) {
            salesReportDTO.setTotalProfit(totalProfit);
            return this;
        }

        public SalesReportDTO build() {
            return salesReportDTO;
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

