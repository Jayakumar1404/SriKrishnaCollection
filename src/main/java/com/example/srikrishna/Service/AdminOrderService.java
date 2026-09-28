package com.example.srikrishna.Service;

import java.util.List;

import com.example.srikrishna.Entity.Order;

public interface AdminOrderService {

    // =========================================================
    // GET ALL ORDERS
    // =========================================================

    List<Order> getAllOrders();


    // =========================================================
    // GET ORDER BY ID
    // =========================================================

    Order getOrderById(Long orderId);


    // =========================================================
    // UPDATE ORDER STATUS
    // =========================================================

    void updateOrderStatus(
            Long orderId,
            String orderStatus
    );
}