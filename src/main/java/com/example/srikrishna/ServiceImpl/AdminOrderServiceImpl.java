package com.example.srikrishna.ServiceImpl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.srikrishna.Entity.Order;
import com.example.srikrishna.Repository.OrderRepository;
import com.example.srikrishna.Service.AdminOrderService;

@Service
public class AdminOrderServiceImpl implements AdminOrderService {

    private final OrderRepository orderRepository;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public AdminOrderServiceImpl(
            OrderRepository orderRepository) {

        this.orderRepository = orderRepository;
    }


    // =========================================================
    // GET ALL ORDERS
    // =========================================================

    @Override
    public List<Order> getAllOrders() {

        return orderRepository.findAll();
    }


    // =========================================================
    // GET ORDER BY ID
    // =========================================================

    @Override
    public Order getOrderById(
            Long orderId) {

        return orderRepository
                .findById(orderId)
                .orElse(null);
    }


    // =========================================================
    // UPDATE ORDER STATUS
    // =========================================================

    @Override
    @Transactional
    public void updateOrderStatus(
            Long orderId,
            String orderStatus) {


        // -----------------------------------------------------
        // FIND ORDER
        // -----------------------------------------------------

        Order order =
                orderRepository
                        .findById(orderId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Order not found: "
                                                + orderId
                                )
                        );


        // -----------------------------------------------------
        // VALIDATE STATUS
        // -----------------------------------------------------

        if (orderStatus == null
                || orderStatus.trim().isEmpty()) {

            throw new RuntimeException(
                    "Order status is required."
            );
        }


        String status =
                orderStatus
                        .trim()
                        .toUpperCase();


        // -----------------------------------------------------
        // ALLOWED STATUSES
        // -----------------------------------------------------

        if (!status.equals("PLACED")
                && !status.equals("CONFIRMED")
                && !status.equals("PACKED")
                && !status.equals("SHIPPED")
                && !status.equals("DELIVERED")
                && !status.equals("CANCELLED")) {

            throw new RuntimeException(
                    "Invalid order status: "
                            + status
            );
        }


        // -----------------------------------------------------
        // UPDATE
        // -----------------------------------------------------

        order.setOrderStatus(status);


        // -----------------------------------------------------
        // SAVE
        // -----------------------------------------------------

        orderRepository.save(order);
    }
}