package com.enagbem.qm_inventory.controller;

import com.enagbem.qm_inventory.exception.BusinessException;
import com.enagbem.qm_inventory.exception.ResourceNotFoundException;
import com.enagbem.qm_inventory.service.InvoiceService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/documents")
@CrossOrigin(origins = "*")
public class InvoiceController {
    private static final Logger logger = LoggerFactory.getLogger(InvoiceController.class);
    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("/invoice/{orderId}")
    public ResponseEntity<byte[]> generateInvoice(@PathVariable Long orderId) {
        logger.info("Generating invoice for order: {}", orderId);
        try {
            byte[] pdfContent = invoiceService.generateInvoice(orderId);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "invoice-" + orderId + ".pdf");
            logger.info("Successfully generated invoice for order: {}", orderId);
            return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            logger.error("Order not found for invoice generation: {}", orderId);
            throw e;
        } catch (BusinessException e) {
            logger.error("Business error while generating invoice for order {}: {}", orderId, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error generating invoice for order {}: {}", orderId, e.getMessage());
            throw new BusinessException("Failed to generate invoice: " + e.getMessage());
        }
    }

    @GetMapping("/receipt/{orderId}")
    public ResponseEntity<byte[]> generateReceipt(@PathVariable Long orderId) {
        logger.info("Generating receipt for order: {}", orderId);
        try {
            byte[] pdfContent = invoiceService.generateReceipt(orderId);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "receipt-" + orderId + ".pdf");
            logger.info("Successfully generated receipt for order: {}", orderId);
            return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            logger.error("Order not found for receipt generation: {}", orderId);
            throw e;
        } catch (BusinessException e) {
            logger.error("Business error while generating receipt for order {}: {}", orderId, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error generating receipt for order {}: {}", orderId, e.getMessage());
            throw new BusinessException("Failed to generate receipt: " + e.getMessage());
        }
    }
}