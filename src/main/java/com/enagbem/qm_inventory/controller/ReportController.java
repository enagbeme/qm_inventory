package com.enagbem.qm_inventory.controller;

import com.enagbem.qm_inventory.dto.SalesReportDTO;
import com.enagbem.qm_inventory.repository.UserRepository;
import com.enagbem.qm_inventory.service.ReportService;
import com.enagbem.qm_inventory.service.PdfGenerationService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;
    private final UserRepository userRepository;
    private final PdfGenerationService pdfGenerationService;

    // Helper method to add user to model
    private void addUserToModel(Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            userRepository.findByUsername(username).ifPresent(user -> model.addAttribute("user", user));
        }
    }

    public ReportController(ReportService reportService, UserRepository userRepository, PdfGenerationService pdfGenerationService) {
        this.reportService = reportService;
        this.userRepository = userRepository;
        this.pdfGenerationService = pdfGenerationService;
    }

    @GetMapping
    public String showReportManagement(Model model, Principal principal) {
        addUserToModel(model, principal);
        return "report-management";
    }

    @GetMapping("/sales")
    public String getSalesReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Model model,
            Principal principal) {

        addUserToModel(model, principal);
        if (startDate == null) {
            startDate = LocalDateTime.now().minusMonths(1);
        }
        if (endDate == null) {
            endDate = LocalDateTime.now();
        }
        List<SalesReportDTO> report = reportService.generateSalesReport(startDate, endDate);
        model.addAttribute("salesReport", report);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        BigDecimal totalRevenue = reportService.calculateTotalRevenue(startDate, endDate);
        Long orderCount = reportService.countOrders(startDate, endDate);
        
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("orderCount", orderCount);
        
        return "report-management";
    }

    @GetMapping("/sales/pdf")
    public ResponseEntity<InputStreamResource> exportSalesPdf(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        if (startDate == null) {
            startDate = LocalDateTime.now().minusMonths(1);
        }
        if (endDate == null) {
            endDate = LocalDateTime.now();
        }

        List<SalesReportDTO> report = reportService.generateSalesReport(startDate, endDate);
        BigDecimal totalRevenue = reportService.calculateTotalRevenue(startDate, endDate);
        Long orderCount = reportService.countOrders(startDate, endDate);

        ByteArrayInputStream bis = pdfGenerationService.generateSalesReport(
                report, startDate, endDate, 
                String.format("$%.2f", totalRevenue), orderCount);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=sales-report.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));

    }

    @GetMapping("/products/pdf")
    public ResponseEntity<InputStreamResource> exportProductsPdf() {
        ByteArrayInputStream bis = pdfGenerationService.generateProductsReport();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=products-report.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    @GetMapping("/inventory/pdf")
    public ResponseEntity<InputStreamResource> exportInventoryPdf() {
        ByteArrayInputStream bis = pdfGenerationService.generateInventoryReport();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=inventory-report.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}