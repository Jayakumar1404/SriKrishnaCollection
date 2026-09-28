package com.example.srikrishna.Repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.srikrishna.Entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // =====================================================
    // CUSTOMER ORDERS
    // =====================================================

    List<Order> findByCustomerIdOrderByOrderDateDesc(
            Long customerId
    );


    // =====================================================
    // SEARCH BY NAME OR EMAIL
    // =====================================================

    List<Order>
    findByCustomerFirstNameContainingIgnoreCaseOrCustomerEmailContainingIgnoreCaseOrderByOrderDateDesc(
            String firstName,
            String email
    );


    // =====================================================
    // FILTER BY STATUS
    // =====================================================

    List<Order> findByOrderStatusOrderByOrderDateDesc(
            String orderStatus
    );


    // =====================================================
    // SEARCH + STATUS
    // =====================================================

    @Query("""
        SELECT o
        FROM Order o
        LEFT JOIN o.customer c
        WHERE
            (
                LOWER(c.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR
                LOWER(c.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR
                LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR
                CAST(o.id AS string) LIKE CONCAT('%', :keyword, '%')
            )
            AND
            (
                :status = 'ALL'
                OR
                UPPER(o.orderStatus) = UPPER(:status)
            )
        ORDER BY o.orderDate DESC
        """)
    List<Order> searchAndFilterOrders(
            @Param("keyword") String keyword,
            @Param("status") String status
    );


    // =====================================================
    // TODAY'S ORDERS
    // =====================================================

    long countByOrderDateBetween(
            LocalDateTime start,
            LocalDateTime end
    );


    // =====================================================
    // COUNT BY STATUS
    // =====================================================

    long countByOrderStatus(String orderStatus);


    // =====================================================
    // TOTAL REVENUE
    // =====================================================

    @Query("""
        SELECT COALESCE(SUM(o.totalAmount), 0)
        FROM Order o
        WHERE UPPER(o.orderStatus) <> 'CANCELLED'
        """)
    Double getTotalRevenue();


    // =====================================================
    // TODAY'S REVENUE
    // =====================================================

    @Query("""
        SELECT COALESCE(SUM(o.totalAmount), 0)
        FROM Order o
        WHERE o.orderDate >= :start
          AND o.orderDate < :end
          AND UPPER(o.orderStatus) <> 'CANCELLED'
        """)
    Double getTodayRevenue(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

}