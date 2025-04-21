package com.enagbem.qm_inventory.controller;

import com.enagbem.qm_inventory.dto.OrderDTO;
import com.enagbem.qm_inventory.dto.ProductDTO;
import com.enagbem.qm_inventory.dto.PurchaseOrderDTO;
import com.enagbem.qm_inventory.repository.UserRepository;
import com.enagbem.qm_inventory.service.CustomerService;
import com.enagbem.qm_inventory.service.OrderService;
import com.enagbem.qm_inventory.service.ProductService;
import com.enagbem.qm_inventory.service.PurchaseOrderService;
import com.enagbem.qm_inventory.service.SupplierService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.*;

@Controller
public class HomeController {

    private final UserRepository userRepository;
    private final OrderService orderService;
    private final ProductService productService;
    private final SupplierService supplierService;
    private final CustomerService customerService;
    private final PurchaseOrderService purchaseOrderService;

    @Autowired
    public HomeController(UserRepository userRepository,
                          OrderService orderService,
                          ProductService productService,
                          SupplierService supplierService,
                          CustomerService customerService,
                          PurchaseOrderService purchaseOrderService) {
        this.userRepository = userRepository;
        this.orderService = orderService;
        this.productService = productService;
        this.supplierService = supplierService;
        this.customerService = customerService;
        this.purchaseOrderService = purchaseOrderService;
    }

    @GetMapping("/")
    public String homePage(Model model, Principal principal) {
        userRepository.findByUsername(principal.getName())
                .ifPresent(u -> model.addAttribute("user", u));

        DashboardStats stats = getDashboardStats();
        model.addAttribute("dashboardStats", stats);

        model.addAttribute("recentOrders",
                orderService.getAllOrders().stream().limit(5).toList());
        model.addAttribute("inventoryDistribution", getInventoryDistribution());
        model.addAttribute("monthlySales", getMonthlySalesData());

        return "home";
    }

    private Map<String, Integer> getInventoryDistribution() {
        Map<String, Integer> dist = new HashMap<>();
        for (ProductDTO p : productService.getAllProducts()) {
            String cat = p.getCategoryName() != null ? p.getCategoryName() : "Uncategorized";
            dist.merge(cat, p.getCurrentStock(), Integer::sum);
        }
        return dist;
    }

    private Map<String, Double> getMonthlySalesData() {
        Map<String, Double> sales = new TreeMap<>();
        String[] months = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        for (String m : months) sales.put(m, 0.0);

        int year = Calendar.getInstance().get(Calendar.YEAR);
        for (OrderDTO o : orderService.getAllOrders()) {
            if (o.getStatus() == com.enagbem.qm_inventory.model.Order.OrderStatus.DELIVERED) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(o.getOrderDate());
                if (cal.get(Calendar.YEAR) == year) {
                    String key = months[cal.get(Calendar.MONTH)];
                    sales.merge(key, o.getTotalAmount().doubleValue(), Double::sum);
                }
            }
        }
        return sales;
    }

    private DashboardStats getDashboardStats() {
        var products    = productService.getAllProducts();
        var allOrders   = orderService.getAllOrders();
        var purchasePos = purchaseOrderService.getAllPurchaseOrders();

        int totalItems     = products.size();
        int totalSuppliers = supplierService.getAllSuppliers().size();
        int totalCustomers = customerService.getAllCustomers().size();

        double stockValue = products.stream()
                .mapToDouble(p -> p.getPrice().doubleValue() * p.getCurrentStock())
                .sum();

        int totalOrders     = allOrders.size();
        int completedOrders = (int) allOrders.stream()
                .filter(o -> o.getStatus() == com.enagbem.qm_inventory.model.Order.OrderStatus.DELIVERED)
                .count();
        int pendingOrders = totalOrders - completedOrders;

        double totalRevenue = allOrders.stream()
                .filter(o -> o.getStatus() == com.enagbem.qm_inventory.model.Order.OrderStatus.DELIVERED)
                .map(OrderDTO::getTotalAmount)
                .mapToDouble(BigDecimal::doubleValue)
                .sum();

        // Total Purchases: unit price from purchase items × quantity
        double totalPurchases = purchasePos.stream()
                .flatMap(po -> po.getItems().stream())
                .mapToDouble(item -> item.getUnitCost().doubleValue() * item.getQuantity())
                .sum();

        int totalPurchaseOrders = purchasePos.size();
        int completedPOs = (int) purchasePos.stream()
                .filter(po -> po.getStatus() == com.enagbem.qm_inventory.model.PurchaseOrder.PurchaseOrderStatus.RECEIVED)
                .count();
        int pendingPOs = totalPurchaseOrders - completedPOs;

        return new DashboardStats(
                totalItems,
                totalSuppliers,
                totalCustomers,
                stockValue,
                totalRevenue,
                totalPurchases,
                totalOrders,
                pendingOrders,
                completedOrders,
                totalPurchaseOrders,
                pendingPOs,
                completedPOs
        );
    }

    @Getter
    private static class DashboardStats {
        private final int totalItems;
        private final int totalSuppliers;
        private final int totalCustomers;
        private final double stockValue;
        private final double totalRevenue;
        private final double totalPurchases;
        private final int totalOrders;
        private final int pendingOrders;
        private final int completedOrders;
        private final int totalPurchaseOrders;
        private final int pendingPurchaseOrders;
        private final int completedPurchaseOrders;

        public DashboardStats(int totalItems,
                              int totalSuppliers,
                              int totalCustomers,
                              double stockValue,
                              double totalRevenue,
                              double totalPurchases,
                              int totalOrders,
                              int pendingOrders,
                              int completedOrders,
                              int totalPurchaseOrders,
                              int pendingPurchaseOrders,
                              int completedPurchaseOrders) {
            this.totalItems              = totalItems;
            this.totalSuppliers          = totalSuppliers;
            this.totalCustomers          = totalCustomers;
            this.stockValue              = stockValue;
            this.totalRevenue            = totalRevenue;
            this.totalPurchases          = totalPurchases;
            this.totalOrders             = totalOrders;
            this.pendingOrders           = pendingOrders;
            this.completedOrders         = completedOrders;
            this.totalPurchaseOrders     = totalPurchaseOrders;
            this.pendingPurchaseOrders   = pendingPurchaseOrders;
            this.completedPurchaseOrders = completedPurchaseOrders;
        }
    }
}