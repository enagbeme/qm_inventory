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
        String username = principal.getName();
        userRepository.findByUsername(username).ifPresent(user -> model.addAttribute("user", user));

        DashboardStats dashboardStats = getDashboardStats();
        model.addAttribute("dashboardStats", dashboardStats);

        List<OrderDTO> recentOrders = orderService.getAllOrders().stream().limit(5).toList();
        model.addAttribute("recentOrders", recentOrders);

        // Add inventory distribution data
        Map<String, Integer> inventoryDistribution = getInventoryDistribution();
        model.addAttribute("inventoryDistribution", inventoryDistribution);

        // Add monthly sales data
        Map<String, Double> monthlySales = getMonthlySalesData();
        model.addAttribute("monthlySales", monthlySales);

        return "home";
    }

    private Map<String, Integer> getInventoryDistribution() {
        Map<String, Integer> distribution = new HashMap<>();
        
        // Get all products
        List<ProductDTO> products = productService.getAllProducts();
        
        // Group products by category and count
        products.forEach(product -> {
            String category = product.getCategoryName() != null ? 
                             product.getCategoryName() : 
                             "Uncategorized";
            distribution.merge(category, product.getCurrentStock(), Integer::sum);
        });
        
        return distribution;
    }

    private Map<String, Double> getMonthlySalesData() {
        Map<String, Double> monthlySales = new TreeMap<>(); // TreeMap to maintain chronological order
        
        // Get current year
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        
        // Initialize all months with 0.0
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        for (String month : months) {
            monthlySales.put(month, 0.0);
        }
        
        // Calculate sales for each month
        List<OrderDTO> allOrders = orderService.getAllOrders();
        for (OrderDTO order : allOrders) {
            if (order.getStatus() == com.enagbem.qm_inventory.model.Order.OrderStatus.DELIVERED) {
                Calendar orderDate = Calendar.getInstance();
                orderDate.setTime(order.getOrderDate());
                
                // Only include orders from current year
                if (orderDate.get(Calendar.YEAR) == currentYear) {
                    int monthIndex = orderDate.get(Calendar.MONTH);
                    String monthKey = months[monthIndex];
                    double currentTotal = monthlySales.get(monthKey);
                    monthlySales.put(monthKey, currentTotal + order.getTotalAmount().doubleValue());
                }
            }
        }
        
        return monthlySales;
    }

    private DashboardStats getDashboardStats() {
        int totalItems = productService.getAllProducts().size();
        int totalSuppliers = supplierService.getAllSuppliers().size();
        int totalCustomers = customerService.getAllCustomers().size();

        // Stock Value = price * currentStock
        double stockValue = productService.getAllProducts().stream()
                .mapToDouble(product -> product.getPrice().doubleValue() * product.getCurrentStock())
                .sum();

        // Get all orders
        List<OrderDTO> allOrders = orderService.getAllOrders();
        int totalOrders = allOrders.size();
        
        // Count orders by status
        int completedOrders = (int) allOrders.stream()
                .filter(order -> order.getStatus() == com.enagbem.qm_inventory.model.Order.OrderStatus.DELIVERED)
                .count();
        int pendingOrders = totalOrders - completedOrders;

        // Revenue = sum of totalAmount of delivered orders
        double totalRevenue = allOrders.stream()
                .filter(order -> order.getStatus() == com.enagbem.qm_inventory.model.Order.OrderStatus.DELIVERED)
                .map(OrderDTO::getTotalAmount)
                .mapToDouble(BigDecimal::doubleValue)
                .sum();

        // Expenditure = sum of (costPrice * quantitySold) for each product
        Map<Long, Integer> productSalesMap = new HashMap<>();
        allOrders.stream()
                .filter(order -> order.getStatus() == com.enagbem.qm_inventory.model.Order.OrderStatus.DELIVERED)
                .flatMap(order -> order.getOrderItems().stream())
                .forEach(item -> productSalesMap.merge(
                        item.getProductId(),
                        item.getQuantity(),
                        Integer::sum
                ));

        double totalExpenditure = productService.getAllProducts().stream()
                .filter(product -> productSalesMap.containsKey(product.getProductId()))
                .mapToDouble(product -> {
                    int quantitySold = productSalesMap.get(product.getProductId());
                    return product.getCostPrice().doubleValue() * quantitySold;
                })
                .sum();

        // Get purchase order statistics
        List<PurchaseOrderDTO> allPurchaseOrders = purchaseOrderService.getAllPurchaseOrders();
        int totalPurchaseOrders = allPurchaseOrders.size();
        
        // Count purchase orders by status
        int completedPurchaseOrders = (int) allPurchaseOrders.stream()
                .filter(po -> po.getStatus() == com.enagbem.qm_inventory.model.PurchaseOrder.PurchaseOrderStatus.RECEIVED)
                .count();
        int pendingPurchaseOrders = totalPurchaseOrders - completedPurchaseOrders;

        double totalProjectedIncome = stockValue - totalExpenditure;

        return new DashboardStats(
                totalItems,
                totalSuppliers,
                totalCustomers,
                stockValue,
                totalExpenditure,
                totalRevenue,
                totalProjectedIncome,
                totalOrders,
                pendingOrders,
                completedOrders,
                totalPurchaseOrders,
                pendingPurchaseOrders,
                completedPurchaseOrders
        );
    }

    @Getter
    private static class DashboardStats {
        private final int totalItems;
        private final int totalSuppliers;
        private final int totalCustomers;
        private final double stockValue;
        private final double totalRevenue;
        private final double totalExpenditure;
        private final double totalProjectedIncome;
        private final int totalOrders;
        private final int pendingOrders;
        private final int completedOrders;
        private final int totalPurchaseOrders;
        private final int pendingPurchaseOrders;
        private final int completedPurchaseOrders;

        public DashboardStats(int totalItems, int totalSuppliers, int totalCustomers,
                             double stockValue, double totalRevenue, double totalExpenditure,
                             double totalProjectedIncome, int totalOrders, int pendingOrders,
                             int completedOrders, int totalPurchaseOrders, int pendingPurchaseOrders,
                             int completedPurchaseOrders) {
            this.totalItems = totalItems;
            this.totalSuppliers = totalSuppliers;
            this.totalCustomers = totalCustomers;
            this.stockValue = stockValue;
            this.totalRevenue = totalRevenue;
            this.totalExpenditure = totalExpenditure;
            this.totalProjectedIncome = totalProjectedIncome;
            this.totalOrders = totalOrders;
            this.pendingOrders = pendingOrders;
            this.completedOrders = completedOrders;
            this.totalPurchaseOrders = totalPurchaseOrders;
            this.pendingPurchaseOrders = pendingPurchaseOrders;
            this.completedPurchaseOrders = completedPurchaseOrders;
        }

        public int getTotalItems() { return totalItems; }

        public int getTotalSuppliers() { return totalSuppliers; }

        public int getTotalCustomers() { return totalCustomers; }

        public double getStockValue() { return stockValue; }

        public double getTotalExpenditure() { return totalExpenditure; }

        public double getTotalRevenue() { return totalRevenue; }

        public double getTotalProjectedIncome() { 
                return totalProjectedIncome; }
    }
}
