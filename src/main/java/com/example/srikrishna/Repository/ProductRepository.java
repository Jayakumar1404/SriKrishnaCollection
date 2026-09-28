package com.example.srikrishna.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.srikrishna.Entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // =====================================================
    // ALL ACTIVE PRODUCTS
    // =====================================================

    List<Product> findByStatusTrue();

    // =====================================================
    // ACTIVE PRODUCTS BY CATEGORY
    // =====================================================

    List<Product> findByCategory_NameIgnoreCaseAndStatusTrue(
            String categoryName);
}