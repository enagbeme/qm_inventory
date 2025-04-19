package com.enagbem.qm_inventory.controller;

import com.enagbem.qm_inventory.dto.CustomerDTO;
import com.enagbem.qm_inventory.dto.OrderDTO;
import com.enagbem.qm_inventory.dto.ProductDTO;
import com.enagbem.qm_inventory.exception.BusinessException;
import com.enagbem.qm_inventory.exception.ResourceNotFoundException;
import com.enagbem.qm_inventory.model.Order;
import com.enagbem.qm_inventory.repository.UserRepository;
import com.enagbem.qm_inventory.service.CustomerService;
import com.enagbem.qm_inventory.service.OrderService;
import com.enagbem.qm_inventory.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class WebOrderController {

    private final OrderService orderService;
    private final CustomerService customerService;
    private final ProductService productService;
    private final UserRepository userRepository;

    public WebOrderController(OrderService orderService, CustomerService customerService, ProductService productService, UserRepository userRepository) {
        this.orderService = orderService;
        this.customerService = customerService;
        this.productService = productService;
        this.userRepository = userRepository;
    }

    private void addUserToModel(Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            userRepository.findByUsername(username).ifPresent(user -> model.addAttribute("user", user));
        }
    }

    @GetMapping
    public String listOrders(Model model, Principal principal) {
        addUserToModel(model, principal);
        List<OrderDTO> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);

        return "orders";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model, Principal principal) {
        addUserToModel(model, principal);
        model.addAttribute("orderDTO", new OrderDTO()); // Changed from "order" to "orderDTO"
        model.addAttribute("customers", customerService.getAllCustomers());
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("orderStatuses", Order.OrderStatus.values());
        return "order-form";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, Principal principal) {
        addUserToModel(model, principal);
        model.addAttribute("orderDTO", orderService.getOrderById(id)); // Changed from "order" to "orderDTO"
        model.addAttribute("customers", customerService.getAllCustomers());
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("orderStatuses", Order.OrderStatus.values());
        return "order-form";
    }

    @PostMapping("/new")
    public String createOrder(@ModelAttribute OrderDTO orderDTO, RedirectAttributes redirectAttributes) {
        try {
            orderService.createOrder(orderDTO);
            redirectAttributes.addFlashAttribute("message", "Order created successfully!");
            return "redirect:/orders";
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", "Customer or product not found: " + e.getMessage());
            return "redirect:/orders/new";
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/orders/new";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An unexpected error occurred while creating the order");
            return "redirect:/orders/new";
        }
    }


    @PostMapping("/{id}")
    public String updateOrder(@PathVariable Long id, @ModelAttribute OrderDTO orderDTO, RedirectAttributes redirectAttributes) {
        try {
            // Get current order to preserve status
            orderService.updateOrder(id, orderDTO);
            redirectAttributes.addFlashAttribute("message", "Order updated successfully!");
            return "redirect:/orders";
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", "Customer or product not found: " + e.getMessage());
            return "redirect:/orders/" + id + "/edit";
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/orders/" + id + "/edit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An unexpected error occurred while updating the order");
            return "redirect:/orders/" + id + "/edit";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            orderService.deleteOrder(id);
            redirectAttributes.addFlashAttribute("message", "Order deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting order: " + e.getMessage());
        }
        return "redirect:/orders";
    }

    @GetMapping("/{id}")
    public String viewOrder(@PathVariable Long id, Model model, Principal principal) {
        addUserToModel(model, principal);
        try {
            OrderDTO order = orderService.getOrderById(id);
            model.addAttribute("order", order);
            return "order-details";
        } catch (ResourceNotFoundException e) {
            model.addAttribute("error", "Order not found: " + e.getMessage());
            return "redirect:/orders";
        }
    }

    @PostMapping("/{id}/status")
    public String updateOrderStatus(@PathVariable Long id, @RequestParam Order.OrderStatus status,
                                  RedirectAttributes redirectAttributes, Principal principal) {
        try {
            OrderDTO updatedOrder = orderService.updateOrderStatus(id, status);
            redirectAttributes.addFlashAttribute("successMessage", "Order status updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating order status: " + e.getMessage());
        }
        return "redirect:/orders";
    }


    @DeleteMapping("/{id}")
    @ResponseBody
    public String deleteOrder(@PathVariable Long id, Principal principal) {
        try {
            orderService.deleteOrder(id);
            return "success";
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }
}