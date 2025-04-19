package com.enagbem.qm_inventory.repository;


import com.enagbem.qm_inventory.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatus(Order.OrderStatus status);
    List<Order> findByOrderDateBetween(Date orderDate, LocalDateTime orderDate2);
    List<Order> findByCustomer_CustomerId(Long customerId);

    @Query("SELECT o FROM Order o WHERE o.orderDate >= :startDate AND o.status = 'DELIVERED'")
    List<Order> findCompletedOrdersSince(@Param("startDate") LocalDateTime startDate);

    @Query("SELECT oi.product.productId, SUM(oi.quantity) FROM Order o JOIN o.items oi " +
            "WHERE o.status = 'DELIVERED' AND o.orderDate BETWEEN :start AND :end " +
            "GROUP BY oi.product.productId")
    List<Object[]> findProductSalesBetweenDates(@Param("start") LocalDateTime start,
                                                @Param("end") LocalDateTime end);

    @Query("SELECT oi.product.productId, SUM(oi.quantity) FROM Order o JOIN o.items oi " +
            "WHERE oi.product.productId = :productId AND o.orderDate >= :startDate " +
            "AND o.status = 'DELIVERED' GROUP BY oi.product.productId")
    List<Object[]> findProductSalesHistory(@Param("productId") Long productId, @Param("startDate") LocalDateTime startDate);



    @Query("SELECT oi.product.productId, oi.product.name, oi.product.category.name, SUM(oi.quantity), SUM(oi.unitPrice), SUM(oi.quantity * oi.unitPrice) " +
            "FROM Order o JOIN o.items oi " +
            "WHERE o.orderDate BETWEEN :startDate AND :endDate " +
            "GROUP BY oi.product.productId, oi.product.name, oi.product.category.name")
    List<Object[]> findSalesReportData(@Param("startDate") LocalDateTime startDate,
                                       @Param("endDate") LocalDateTime endDate);


    @Query("SELECT SUM(oi.quantity * oi.unitPrice) " +
            "FROM Order o JOIN o.items oi " +
            "WHERE o.orderDate BETWEEN :startDate AND :endDate")
    BigDecimal calculateTotalRevenue(@Param("startDate") LocalDateTime startDate,
                                     @Param("endDate") LocalDateTime endDate);



    @Query("SELECT COUNT(o) " +
            "FROM Order o " +
            "WHERE o.orderDate BETWEEN :startDate AND :endDate " +
            "AND o.status = :orderStatus")
    Long countByOrderDateBetweenAndStatus(@Param("startDate") LocalDateTime startDate,
                                          @Param("endDate") LocalDateTime endDate,
                                          @Param("orderStatus") Order.OrderStatus orderStatus);

}
