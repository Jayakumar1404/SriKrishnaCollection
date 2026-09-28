package com.example.srikrishna.ServiceImpl;

import java.time.LocalDateTime;
import java.util.List;


import org.springframework.stereotype.Service;
import com.example.srikrishna.Service.*;
import com.example.srikrishna.Entity.Order;
import com.example.srikrishna.Repository.OrderRepository;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public OrderServiceImpl(
            OrderRepository orderRepository) {

        this.orderRepository = orderRepository;
    }


    // =====================================================
    // GET SINGLE ORDER
    // =====================================================

    @Override
    public Order getOrderById(Long id) {

        return orderRepository
                .findById(id)
                .orElse(null);
    }


    // =====================================================
    // GET ALL ORDERS
    // =====================================================

    @Override
    public List<Order> getAllOrders() {

        return orderRepository.findAll();
    }


    // =====================================================
    // GET CUSTOMER ORDERS
    // =====================================================

    @Override
    public List<Order> getOrdersByCustomerId(
            Long customerId) {

        return orderRepository
                .findByCustomerIdOrderByOrderDateDesc(
                        customerId
                );
    }


    // =====================================================
    // CANCEL ORDER
    // =====================================================

    @Override
    public void cancelOrder(Long orderId) {

        Order order = orderRepository
                .findById(orderId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Order not found"
                        )
                );


        String currentStatus =
                order.getOrderStatus();


        if ("DELIVERED".equalsIgnoreCase(
                currentStatus)) {

            throw new RuntimeException(
                    "Delivered order cannot be cancelled"
            );
        }


        if ("SHIPPED".equalsIgnoreCase(
                currentStatus)) {

            throw new RuntimeException(
                    "Shipped order cannot be cancelled"
            );
        }


        if ("CANCELLED".equalsIgnoreCase(
                currentStatus)) {

            throw new RuntimeException(
                    "Order is already cancelled"
            );
        }


        order.setOrderStatus("CANCELLED");

        order.setPaymentStatus(
                "REFUND_PENDING"
        );

        orderRepository.save(order);
    }


    // =====================================================
    // UPDATE ORDER STATUS
    // =====================================================

    @Override
    public void updateOrderStatus(
            Long orderId,
            String status) {

        Order order = orderRepository
                .findById(orderId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Order not found"
                        )
                );


        if (status == null
                || status.trim().isEmpty()) {

            throw new RuntimeException(
                    "Order status cannot be empty"
            );
        }


        String newStatus =
                status.trim().toUpperCase();


        switch (newStatus) {

            case "PLACED":
            case "CONFIRMED":
            case "PACKED":
            case "SHIPPED":
            case "DELIVERED":
            case "CANCELLED":

                order.setOrderStatus(
                        newStatus
                );

                break;


            default:

                throw new RuntimeException(
                        "Invalid order status"
                );
        }


        if ("CANCELLED".equals(newStatus)) {

            order.setPaymentStatus(
                    "REFUND_PENDING"
            );
        }


        orderRepository.save(order);
    }


    // =====================================================
    // SEARCH ORDERS
    // =====================================================

    @Override
    public List<Order> searchOrders(
            String keyword) {

        if (keyword == null
                || keyword.trim().isEmpty()) {

            return orderRepository.findAll();
        }


        String search =
                keyword.trim();


        return orderRepository
                .findByCustomerFirstNameContainingIgnoreCaseOrCustomerEmailContainingIgnoreCaseOrderByOrderDateDesc(
                        search,
                        search
                );
    }


    // =====================================================
    // FILTER BY STATUS
    // =====================================================

    @Override
    public List<Order> getOrdersByStatus(
            String status) {

        if (status == null
                || status.trim().isEmpty()
                || "ALL".equalsIgnoreCase(status)) {

            return orderRepository.findAll();
        }


        return orderRepository
                .findByOrderStatusOrderByOrderDateDesc(
                        status.trim().toUpperCase()
                );
    }


    // =====================================================
    // SEARCH + STATUS FILTER
    // =====================================================

    @Override
    public List<Order> searchAndFilterOrders(
            String keyword,
            String status) {

        String searchKeyword =
                keyword == null
                        ? ""
                        : keyword.trim();


        String searchStatus =
                status == null
                        || status.trim().isEmpty()
                        ? "ALL"
                        : status.trim().toUpperCase();


        return orderRepository
                .searchAndFilterOrders(
                        searchKeyword,
                        searchStatus
                );
    }


    // =====================================================
    // TOTAL ORDERS
    // =====================================================

    @Override
    public long getTotalOrders() {

        return orderRepository.count();
    }


    // =====================================================
    // TODAY'S ORDERS
    // =====================================================

    @Override
    public long getTodayOrders(
            LocalDateTime start,
            LocalDateTime end) {

        return orderRepository
                .countByOrderDateBetween(
                        start,
                        end
                );
    }


    // =====================================================
    // PENDING ORDERS
    // =====================================================

    @Override
    public long getPendingOrders() {

        long placed =
                orderRepository
                        .countByOrderStatus(
                                "PLACED"
                        );


        long confirmed =
                orderRepository
                        .countByOrderStatus(
                                "CONFIRMED"
                        );


        long packed =
                orderRepository
                        .countByOrderStatus(
                                "PACKED"
                        );


        return placed
                + confirmed
                + packed;
    }


    // =====================================================
    // SHIPPED ORDERS
    // =====================================================

    @Override
    public long getShippedOrders() {

        return orderRepository
                .countByOrderStatus(
                        "SHIPPED"
                );
    }


    // =====================================================
    // DELIVERED ORDERS
    // =====================================================

    @Override
    public long getDeliveredOrders() {

        return orderRepository
                .countByOrderStatus(
                        "DELIVERED"
                );
    }


    // =====================================================
    // CANCELLED ORDERS
    // =====================================================

    @Override
    public long getCancelledOrders() {

        return orderRepository
                .countByOrderStatus(
                        "CANCELLED"
                );
    }


    // =====================================================
    // TOTAL REVENUE
    // =====================================================

    @Override
    public Double getTotalRevenue() {

        Double revenue =
                orderRepository.getTotalRevenue();


        return revenue != null
                ? revenue
                : 0.0;
    }


    // =====================================================
    // TODAY'S REVENUE
    // =====================================================

    @Override
    public Double getTodayRevenue(
            LocalDateTime start,
            LocalDateTime end) {

        Double revenue =
                orderRepository.getTodayRevenue(
                        start,
                        end
                );


        return revenue != null
                ? revenue
                : 0.0;
    }

}