package com.example.srikrishna.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.srikrishna.Entity.Cart;

public interface CartRepository
        extends JpaRepository<Cart, Long> {

    List<Cart> findByCustomerId(
            Long customerId
    );

    Cart findByCustomerIdAndProductId(
            Long customerId,
            Long productId
    );

    void deleteByCustomerId(
            Long customerId
    );

    void deleteByProductId(
            Long productId
    );
}