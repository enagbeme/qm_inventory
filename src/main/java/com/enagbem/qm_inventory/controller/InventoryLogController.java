package com.enagbem.qm_inventory.controller;

import com.enagbem.qm_inventory.dto.InventoryLogDTO;
import com.enagbem.qm_inventory.repository.UserRepository;
import com.enagbem.qm_inventory.service.InventoryService;
import com.enagbem.qm_inventory.service.ProductService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/inventoryLogs")
public class InventoryLogController {

    private final InventoryService inventoryService;
    private final ProductService productService;
    private final UserRepository userRepository;

    public InventoryLogController(InventoryService inventoryService,
                                 ProductService productService,
                                 UserRepository userRepository) {
        this.inventoryService = inventoryService;
        this.productService = productService;
        this.userRepository = userRepository;
    }

    // Helper method to add user to model
    private void addUserToModel(Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            userRepository.findByUsername(username).ifPresent(user -> model.addAttribute("user", user));
        }
    }

    // Get all inventory logs
    @GetMapping
    public String getAllInventoryLogs(Model model, Principal principal) {
        addUserToModel(model, principal);
        // Get all products for the dropdown
        model.addAttribute("products", productService.getAllProducts());
        
        // Get all inventory logs for all products
        List<InventoryLogDTO> allLogs = productService.getAllProducts().stream()
            .flatMap(product -> inventoryService.getInventoryHistory(product.getProductId()).stream())
            .toList();
        model.addAttribute("logs", allLogs);
        
        return "inventory-log-management";
    }

    // Get inventory logs for a specific product
    @GetMapping("/product/{productId}")
    public String getInventoryLogsByProduct(@PathVariable Long productId, Model model, Principal principal) {
        addUserToModel(model, principal);
        List<InventoryLogDTO> logs = inventoryService.getInventoryHistory(productId);
        model.addAttribute("logs", logs);
        model.addAttribute("productId", productId);
        model.addAttribute("products", productService.getAllProducts());
        return "inventory-log-management";
    }

    // Show form to create a new inventory log entry
    @GetMapping("/new")
    public String showCreateInventoryLogForm(Model model, Principal principal) {
        addUserToModel(model, principal);
        model.addAttribute("inventoryLogDTO", new InventoryLogDTO());
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("changeTypes", List.of("PURCHASE", "SALE", "ADJUSTMENT", "RETURN", "DAMAGE"));
        return "inventory-log-form";
    }

    // Create a new inventory log entry
    @PostMapping
    public String createInventoryLog(@ModelAttribute InventoryLogDTO inventoryLogDTO,
                                    RedirectAttributes redirectAttributes,
                                    Principal principal) {
        try {
            String createdBy = principal != null ? principal.getName() : "system";
            inventoryService.adjustInventory(
                    inventoryLogDTO.getProductId(),
                    inventoryLogDTO.getQuantityChange(),
                    inventoryLogDTO.getChangeType(),
                    inventoryLogDTO.getReferenceType(),
                    inventoryLogDTO.getReferenceId(),
                    inventoryLogDTO.getNotes()
            );
            redirectAttributes.addFlashAttribute("message", "Inventory log created successfully!");
            return "redirect:/inventory-logs/product/" + inventoryLogDTO.getProductId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to create inventory log: " + e.getMessage());
            return "redirect:/inventory-logs/new";
        }
    }

    // Get inventory logs between dates
    @GetMapping("/search")
    public String searchInventoryLogs(
            @RequestParam Long productId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Model model,
            Principal principal) {
        
        addUserToModel(model, principal);
        List<InventoryLogDTO> logs;
        
        if (startDate != null && endDate != null) {
            logs = inventoryService.getInventoryChangesBetweenDates(productId, startDate, endDate);
        } else {
            logs = inventoryService.getInventoryHistory(productId);
        }
        
        model.addAttribute("logs", logs);
        model.addAttribute("productId", productId);
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        
        return "inventory-log-management";
    }
}