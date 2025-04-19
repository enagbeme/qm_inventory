package com.enagbem.qm_inventory.service;

import com.enagbem.qm_inventory.dto.OrderDTO;
import com.enagbem.qm_inventory.dto.OrderItemDTO;
import com.enagbem.qm_inventory.exception.BusinessException;
import com.enagbem.qm_inventory.exception.ResourceNotFoundException;
import com.enagbem.qm_inventory.model.Customer;
import com.enagbem.qm_inventory.model.Order;
import com.enagbem.qm_inventory.model.OrderItem;
import com.enagbem.qm_inventory.model.Product;
import com.enagbem.qm_inventory.repository.CustomerRepository;
import com.enagbem.qm_inventory.repository.OrderRepository;
import com.enagbem.qm_inventory.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final InventoryService inventoryService;

    public OrderService(OrderRepository orderRepository, CustomerRepository customerRepository, ProductRepository productRepository, InventoryService inventoryService) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.inventoryService = inventoryService;
    }

    @Transactional(readOnly = true)
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        return convertToDTO(order);
    }

    @Transactional
    public OrderDTO createOrder(OrderDTO orderDTO) {
        Customer customer = customerRepository.findById(orderDTO.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Order order = new Order();
        order.setCustomer(customer);
        order.setStatus(Order.OrderStatus.PENDING);
        order.setPaymentMethod(orderDTO.getPaymentMethod());
        order.setShippingAddress(orderDTO.getShippingAddress());
        order.setNotes(orderDTO.getNotes());

        // Add order items
        for (OrderItemDTO itemDTO : orderDTO.getOrderItems()) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + itemDTO.getProductId()));

            if (product.getCurrentStock() < itemDTO.getQuantity()) {
                throw new BusinessException("Insufficient stock for product: " + product.getName());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setUnitPrice(product.getPrice());

            order.getItems().add(orderItem);
        }

        // Calculate total amount
        BigDecimal totalAmount = order.getItems().stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);
        return convertToDTO(savedOrder);
    }

    @Transactional
    public OrderDTO updateOrder(Long id, OrderDTO orderDTO) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        Customer customer = customerRepository.findById(orderDTO.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        // Update fields including status
        existingOrder.setCustomer(customer);
        existingOrder.setPaymentMethod(orderDTO.getPaymentMethod());
        existingOrder.setShippingAddress(orderDTO.getShippingAddress());
        existingOrder.setNotes(orderDTO.getNotes());
        existingOrder.setStatus(orderDTO.getStatus());  // Directly set the new status

        // Clear existing items and add new ones
        existingOrder.getItems().clear();
        for (OrderItemDTO itemDTO : orderDTO.getOrderItems()) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + itemDTO.getProductId()));

            if (product.getCurrentStock() < itemDTO.getQuantity()) {
                throw new BusinessException("Insufficient stock for product: " + product.getName());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(existingOrder);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setUnitPrice(product.getPrice());

            existingOrder.getItems().add(orderItem);
        }

        // Recalculate total amount
        BigDecimal totalAmount = existingOrder.getItems().stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        existingOrder.setTotalAmount(totalAmount);

        Order updatedOrder = orderRepository.save(existingOrder);
        return convertToDTO(updatedOrder);
    }

    @Transactional
    public OrderDTO updateOrderStatus(Long id, Order.OrderStatus newStatus) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        Order.OrderStatus currentStatus = order.getStatus();

        // Validate order status transition
        if (currentStatus == Order.OrderStatus.CANCELLED) {
            throw new BusinessException("Cannot update a cancelled order");
        }

        if (currentStatus == Order.OrderStatus.DELIVERED) {
            throw new BusinessException("Cannot update a delivered order");
        }

        switch (newStatus) {
            case PROCESSING:
                if (currentStatus != Order.OrderStatus.PENDING) {
                    throw new BusinessException("Order can only be processed from PENDING status. Current status: " + currentStatus);
                }
                break;
            case SHIPPED:
                if (currentStatus != Order.OrderStatus.PROCESSING) {
                    throw new BusinessException("Order can only be shipped from PROCESSING status. Current status: " + currentStatus);
                }
                break;
            case DELIVERED:
                if (currentStatus != Order.OrderStatus.SHIPPED) {
                    throw new BusinessException("Order can only be delivered from SHIPPED status. Current status: " + currentStatus);
                }
                break;
            case CANCELLED:
                if (currentStatus == Order.OrderStatus.DELIVERED) {
                    throw new BusinessException("Cannot cancel a delivered order");
                }
                break;
            case PENDING:
                throw new BusinessException("Cannot set order status back to PENDING");
        }

        order.setStatus(newStatus);
        
        // If order is completed, update inventory
        if (newStatus == Order.OrderStatus.DELIVERED) {
            for (OrderItem item : order.getItems()) {
                inventoryService.adjustInventory(
                        item.getProduct().getProductId(),
                        -item.getQuantity(),
                        "sale",
                        "order",
                        order.getOrderId(),
                        "Order fulfillment"
                );
            }
        }

        Order updatedOrder = orderRepository.save(order);
        return convertToDTO(updatedOrder);
    }

    @Transactional
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<OrderDTO> getOrdersByStatus(Order.OrderStatus status) {
        return orderRepository.findByStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderDTO> getOrdersByCustomer(Long customerId) {
        return orderRepository.findByCustomer_CustomerId(customerId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderDTO> getOrdersBetweenDates(Date start, LocalDateTime end) {
        return orderRepository.findByOrderDateBetween(start, end).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private OrderDTO convertToDTO(Order order) {
        List<OrderItemDTO> itemDTOs = order.getItems().stream()
                .map(this::convertItemToDTO)
                .collect(Collectors.toList());

        return new OrderDTO.Builder()
                .orderId(order.getOrderId())
                .customerId(order.getCustomer().getCustomerId())
                .customer(order.getCustomer()) // Set customer directly
                .orderDate(order.getOrderDate())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .paymentMethod(order.getPaymentMethod())
                .shippingAddress(order.getShippingAddress())
                .notes(order.getNotes())
                .items(itemDTOs)
                .build();
    }

    private OrderItemDTO convertItemToDTO(OrderItem item) {
        return new OrderItemDTO.Builder()
                .orderItemId(item.getOrderItemId())
                .productId(item.getProduct().getProductId())
                .productName(item.getProduct().getName())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .subtotal(item.getSubtotal())
                .build();
    }
}