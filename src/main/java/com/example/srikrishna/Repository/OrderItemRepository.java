package com.example.srikrishna.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.srikrishna.Entity.OrderItem;

public interface OrderItemRepository
        extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrderId(Long orderId);
}