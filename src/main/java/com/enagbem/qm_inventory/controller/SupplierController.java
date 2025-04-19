package com.enagbem.qm_inventory.controller;

import com.enagbem.qm_inventory.dto.SupplierDTO;
import com.enagbem.qm_inventory.exception.DataIntegrityViolationException;
import com.enagbem.qm_inventory.exception.ResourceNotFoundException;
import com.enagbem.qm_inventory.repository.UserRepository;
import com.enagbem.qm_inventory.service.SupplierService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;
    private final UserRepository userRepository;

    // Helper method to add user to model
    private void addUserToModel(Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            userRepository.findByUsername(username).ifPresent(user -> model.addAttribute("user", user));
        }
    }

    // Get all suppliers and render the suppliers page
    @GetMapping
    public String getAllSuppliers(Model model, Principal principal) {
        addUserToModel(model, principal);
        List<SupplierDTO> suppliers = supplierService.getAllSuppliers();
        model.addAttribute("suppliers", suppliers);
        return "suppliers"; // Render the suppliers page
    }

    // Get a single supplier by ID (for AJAX edit form population)
    @GetMapping("/{id}")
    @ResponseBody
    public SupplierDTO getSupplierById(@PathVariable Long id) {
        return supplierService.getSupplierById(id);
    }

    // Show the create supplier form
    @GetMapping("/new")
    public String showCreateSupplierForm(Model model, Principal principal) {
        addUserToModel(model, principal);
        model.addAttribute("supplierDTO", new SupplierDTO());
        return "supplier-form"; // Render the supplier form page
    }

    // Create a new supplier (POST request)
    @PostMapping
    public String createSupplier(@ModelAttribute SupplierDTO supplierDTO) {
        supplierService.createSupplier(supplierDTO);
        return "redirect:/suppliers"; // Redirect back to the suppliers list
    }

    // Show the edit supplier form
    @GetMapping("/{id}/edit")
    public String showEditSupplierForm(@PathVariable Long id, Model model, Principal principal) {
        addUserToModel(model, principal);
        SupplierDTO supplier = supplierService.getSupplierById(id);
        model.addAttribute("supplierDTO", supplier);
        return "supplier-form"; // Render the supplier form page
    }

    // Update an existing supplier (POST request)
    @PostMapping("/{id}")
    public String updateSupplier(@PathVariable Long id, @ModelAttribute SupplierDTO supplierDTO) {
        supplierService.updateSupplier(id, supplierDTO);
        return "redirect:/suppliers"; // Redirect back to the suppliers list
    }

    // Handle the request to delete a supplier.
    @DeleteMapping("/{id}")
    public String deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        return "redirect:/suppliers";
    }

    // Get reliable suppliers (Filter suppliers)
    @GetMapping("/reliable")
    public String getReliableSuppliers(@RequestParam Double minScore, Model model, Principal principal) {
        addUserToModel(model, principal);
        List<SupplierDTO> reliableSuppliers = supplierService.getReliableSuppliers(minScore);
        model.addAttribute("suppliers", reliableSuppliers);
        return "suppliers"; // Return to the suppliers page with the filtered list
    }
}