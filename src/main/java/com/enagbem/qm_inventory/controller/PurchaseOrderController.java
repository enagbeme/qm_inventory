package com.enagbem.qm_inventory.controller;

import com.enagbem.qm_inventory.dto.PurchaseOrderDTO;
import com.enagbem.qm_inventory.exception.BusinessException;
import com.enagbem.qm_inventory.exception.ResourceNotFoundException;
import com.enagbem.qm_inventory.model.PurchaseOrder;
import com.enagbem.qm_inventory.repository.UserRepository;
import com.enagbem.qm_inventory.service.ProductService;
import com.enagbem.qm_inventory.service.PurchaseOrderService;
import com.enagbem.qm_inventory.service.SupplierService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/purchase-orders")
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;
    private final SupplierService supplierService;
    private final UserRepository userRepository;
    private final ProductService productService;

    public PurchaseOrderController(PurchaseOrderService purchaseOrderService,
                                  SupplierService supplierService,
                                  UserRepository userRepository,
                                  ProductService productService) {
        this.purchaseOrderService = purchaseOrderService;
        this.supplierService = supplierService;
        this.userRepository = userRepository;
        this.productService = productService;
    }

    private void addUserToModel(Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            userRepository.findByUsername(username).ifPresent(user -> model.addAttribute("user", user));
        }
    }

    @GetMapping
    public String listPurchaseOrders(Model model, Principal principal) {
        addUserToModel(model, principal);
        List<PurchaseOrderDTO> purchaseOrders = purchaseOrderService.getAllPurchaseOrders();
        model.addAttribute("purchaseOrders", purchaseOrders);
        return "purchase-orders";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model, Principal principal) {
        addUserToModel(model, principal);
        model.addAttribute("purchaseOrderDTO", new PurchaseOrderDTO());
        model.addAttribute("suppliers", supplierService.getAllSuppliers());
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("orderStatuses", PurchaseOrder.PurchaseOrderStatus.values());
        return "purchase-order-form";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, Principal principal) {
        addUserToModel(model, principal);
        model.addAttribute("purchaseOrderDTO", purchaseOrderService.getPurchaseOrderById(id));
        model.addAttribute("suppliers", supplierService.getAllSuppliers());
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("orderStatuses", PurchaseOrder.PurchaseOrderStatus.values());
        return "purchase-order-form";
    }

    @PostMapping("/new")
    public String createPurchaseOrder(@ModelAttribute PurchaseOrderDTO purchaseOrderDTO, RedirectAttributes redirectAttributes) {
        try {
            purchaseOrderService.createPurchaseOrder(purchaseOrderDTO);
            redirectAttributes.addFlashAttribute("message", "Purchase Order created successfully!");
            return "redirect:/purchase-orders";
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", "Supplier or product not found: " + e.getMessage());
            return "redirect:/purchase-orders/new";
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/purchase-orders/new";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/purchase-orders/new";
        }
    }

    @PostMapping("/{id}")
    public String updatePurchaseOrder(@PathVariable Long id, @ModelAttribute PurchaseOrderDTO purchaseOrderDTO,
                                    RedirectAttributes redirectAttributes) {
        try {
            purchaseOrderService.updatePurchaseOrderStatus(id, purchaseOrderDTO.getStatus());
            redirectAttributes.addFlashAttribute("message", "Purchase Order updated successfully!");
            return "redirect:/purchase-orders";
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", "Purchase Order not found: " + e.getMessage());
            return "redirect:/purchase-orders/" + id + "/edit";
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/purchase-orders/" + id + "/edit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An unexpected error occurred while updating the purchase order");
            return "redirect:/purchase-orders/" + id + "/edit";
        }
    }

    @GetMapping("/{id}")
    public String viewPurchaseOrder(@PathVariable Long id, Model model, Principal principal) {
        addUserToModel(model, principal);
        try {
            PurchaseOrderDTO purchaseOrder = purchaseOrderService.getPurchaseOrderById(id);
            model.addAttribute("purchaseOrder", purchaseOrder);
            return "purchase-order-details";
        } catch (ResourceNotFoundException e) {
            model.addAttribute("error", "Purchase Order not found: " + e.getMessage());
            return "redirect:/purchase-orders";
        }
    }

    @PostMapping("/{id}/status")
    public String updatePurchaseOrderStatus(@PathVariable Long id,
                                          @RequestParam PurchaseOrder.PurchaseOrderStatus status,
                                          RedirectAttributes redirectAttributes) {
        try {
            purchaseOrderService.updatePurchaseOrderStatus(id, status);
            redirectAttributes.addFlashAttribute("message", "Purchase Order status updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating purchase order status: " + e.getMessage());
        }
        return "redirect:/purchase-orders";
    }

    @PostMapping("/{id}/delete")
    public String deletePurchaseOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            // Add delete functionality in service and implement here
            redirectAttributes.addFlashAttribute("message", "Purchase Order deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting purchase order: " + e.getMessage());
        }
        return "redirect:/purchase-orders";
    }
}