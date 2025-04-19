package com.enagbem.qm_inventory.dto;

import com.enagbem.qm_inventory.model.Customer;
import com.enagbem.qm_inventory.model.Order;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class OrderDTO {
    private Long orderId;
    private Long customerId;
    private Customer customer; // Updated to hold the Customer object
    private Date orderDate;
    private Order.OrderStatus status;
    private BigDecimal totalAmount;
    private String paymentMethod;
    private String shippingAddress;
    private String notes;
    private List<OrderItemDTO> orderItems;

    public OrderDTO() {}

    public OrderDTO(Long orderId, Long customerId, Customer customer, Date orderDate,
                    Order.OrderStatus status, BigDecimal totalAmount, String paymentMethod,
                    String shippingAddress, String notes, List<OrderItemDTO> orderItems) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.customer = customer; // Initialize customer
        this.orderDate = orderDate;
        this.status = status;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.shippingAddress = shippingAddress;
        this.notes = notes;
        this.orderItems = orderItems;
    }

    // Getters and Setters
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public Date getOrderDate() { return orderDate; }
    public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }

    public Order.OrderStatus getStatus() { return status; }
    public void setStatus(Order.OrderStatus status) { this.status = status; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public List<OrderItemDTO> getOrderItems() { return orderItems; }
    public void setOrderItems(List<OrderItemDTO> orderItems) { this.orderItems = orderItems; }

    public static class Builder {
        private Long orderId;
        private Long customerId;
        private Customer customer; // Updated to hold the Customer object
        private Date orderDate;
        private Order.OrderStatus status;
        private BigDecimal totalAmount;
        private String paymentMethod;
        private String shippingAddress;
        private String notes;
        private List<OrderItemDTO> items;

        public Builder orderId(Long orderId) { this.orderId = orderId; return this; }
        public Builder customerId(Long customerId) { this.customerId = customerId; return this; }
        public Builder customer(Customer customer) { this.customer = customer; return this; } // Updated method
        public Builder orderDate(Date orderDate) { this.orderDate = orderDate; return this; }
        public Builder status(Order.OrderStatus status) { this.status = status; return this; }
        public Builder totalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; return this; }
        public Builder paymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; return this; }
        public Builder shippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; return this; }
        public Builder notes(String notes) { this.notes = notes; return this; }
        public Builder items(List<OrderItemDTO> items) { this.items = items; return this; }

        public OrderDTO build() {
            return new OrderDTO(orderId, customerId, customer, orderDate, status, totalAmount,
                    paymentMethod, shippingAddress, notes, items);
        }
    }
}