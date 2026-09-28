package com.example.srikrishna.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.srikrishna.Entity.CartItem;
import com.example.srikrishna.Entity.Customer;
import com.example.srikrishna.Entity.Product;

public interface CartItemRepository
        extends JpaRepository<CartItem, Long> {

    List<CartItem> findByCustomer(Customer customer);

    Optional<CartItem> findByCustomerAndProduct(
            Customer customer,
            Product product);

    void deleteByCustomer(Customer customer);
}