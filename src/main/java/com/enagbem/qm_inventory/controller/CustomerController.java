package com.enagbem.qm_inventory.controller;

import com.enagbem.qm_inventory.dto.CustomerDTO;
import com.enagbem.qm_inventory.exception.ResourceNotFoundException;
import com.enagbem.qm_inventory.repository.UserRepository;
import com.enagbem.qm_inventory.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final UserRepository userRepository;

    // Helper method to add user to model
    private void addUserToModel(Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            userRepository.findByUsername(username).ifPresent(user -> model.addAttribute("user", user));
        }
    }

    // Get all customers and render the customers page
    @GetMapping
    public String getAllCustomers(Model model, Principal principal) {
        addUserToModel(model, principal);
        List<CustomerDTO> customers = customerService.getAllCustomers();
        model.addAttribute("customers", customers);
        return "customer-management"; // Render the customer management page
    }

    // Show the create customer form
    @GetMapping("/new")
    public String showCreateCustomerForm(Model model, Principal principal) {
        addUserToModel(model, principal);
        model.addAttribute("customerDTO", new CustomerDTO());
        return "customer-form"; // Render the customer form page
    }

    // Show the edit customer form
    @GetMapping("/{id}/edit")
    public String showEditCustomerForm(@PathVariable Long id, Model model, Principal principal) {
        addUserToModel(model, principal);
        CustomerDTO customer = customerService.getCustomerById(id);
        model.addAttribute("customerDTO", customer);
        return "customer-form"; // Render the customer form page
    }

    // Update an existing customer (POST request)
    @PostMapping("/{id}")
    public String updateCustomer(@PathVariable Long id, @ModelAttribute CustomerDTO customerDTO, RedirectAttributes redirectAttributes) {
        customerService.updateCustomer(id, customerDTO);
        redirectAttributes.addFlashAttribute("message", "Customer updated successfully!");
        return "redirect:/customers"; // Redirect back to the customers list
    }

    // Create a new customer (POST request)
    @PostMapping
    public String createCustomer(@ModelAttribute CustomerDTO customerDTO, RedirectAttributes redirectAttributes) {
        customerService.createCustomer(customerDTO);
        redirectAttributes.addFlashAttribute("message", "Customer created successfully!");
        return "redirect:/customers"; // Redirect back to the customers list
    }

    // Handle the request to delete a customer
    @PostMapping("/{id}/delete")
    public String deleteCustomer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        customerService.deleteCustomer(id);
        redirectAttributes.addFlashAttribute("message", "Customer deleted successfully!");
        return "redirect:/customers"; // Redirect back to the customers list
    }

    // Optionally, you can add a method to search for customers
    @GetMapping("/search")
    public String searchCustomers(@RequestParam String query, Model model, Principal principal) {
        addUserToModel(model, principal);
        List<CustomerDTO> customers = customerService.searchCustomers(query);
        model.addAttribute("customers", customers);
        return "customer-management"; // Render the customer management page with search results
    }
}