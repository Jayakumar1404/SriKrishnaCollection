package com.example.srikrishna.Service;

import java.time.LocalDateTime;
import java.util.List;

import com.example.srikrishna.Entity.Order;

public interface OrderService {

    // =====================================================
    // GET SINGLE ORDER
    // =====================================================

    Order getOrderById(Long id);


    // =====================================================
    // GET ALL ORDERS
    // =====================================================

    List<Order> getAllOrders();


    // =====================================================
    // GET CUSTOMER ORDERS
    // =====================================================

    List<Order> getOrdersByCustomerId(Long customerId);


    // =====================================================
    // CANCEL ORDER
    // =====================================================

    void cancelOrder(Long orderId);


    // =====================================================
    // UPDATE ORDER STATUS
    // =====================================================

    void updateOrderStatus(
            Long orderId,
            String status
    );


    // =====================================================
    // SEARCH ORDERS
    // =====================================================

    List<Order> searchOrders(
            String keyword
    );


    // =====================================================
    // FILTER BY STATUS
    // =====================================================

    List<Order> getOrdersByStatus(
            String status
    );


    // =====================================================
    // SEARCH + STATUS FILTER
    // =====================================================

    List<Order> searchAndFilterOrders(
            String keyword,
            String status
    );


    // =====================================================
    // DASHBOARD STATISTICS
    // =====================================================

    long getTotalOrders();


    long getTodayOrders(
            LocalDateTime start,
            LocalDateTime end
    );


    long getPendingOrders();


    long getShippedOrders();


    long getDeliveredOrders();


    long getCancelledOrders();


    Double getTotalRevenue();


    Double getTodayRevenue(
            LocalDateTime start,
            LocalDateTime end
    );

}