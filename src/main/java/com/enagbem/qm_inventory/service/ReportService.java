package com.enagbem.qm_inventory.service;


import com.enagbem.qm_inventory.dto.SalesReportDTO;
import com.enagbem.qm_inventory.model.Order;
import com.enagbem.qm_inventory.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportService {

    private final OrderRepository orderRepository;

    public ReportService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional(readOnly = true)
    public List<SalesReportDTO> generateSalesReport(LocalDateTime startDate, LocalDateTime endDate) {
        List<Object[]> results = orderRepository.findSalesReportData(startDate, endDate);

        List<SalesReportDTO> report = new ArrayList<>();
        for (Object[] row : results) {
            SalesReportDTO dto = new SalesReportDTO();
            dto.setProductId((Long) row[0]);
            dto.setProductName((String) row[1]); // Ensure this is a String
            dto.setCategoryName((String) row[2]); // Ensure this is a String
            dto.setTotalQuantitySold(((Number) row[3]).longValue());
            dto.setTotalRevenue((BigDecimal) row[4]);
            dto.setTotalCost((BigDecimal) row[5]); // If applicable
            dto.setTotalProfit((BigDecimal) row[6]); // If applicable
            report.add(dto);
        }

        return report;
    }

    @Transactional(readOnly = true)
    public BigDecimal calculateTotalRevenue(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.calculateTotalRevenue(startDate, endDate);
    }

    @Transactional(readOnly = true)
    public Long countOrders(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.countByOrderDateBetweenAndStatus(startDate, endDate, Order.OrderStatus.DELIVERED);
    }
}
