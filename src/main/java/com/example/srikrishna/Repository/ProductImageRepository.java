package com.example.srikrishna.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.srikrishna.Entity.ProductImage;

public interface ProductImageRepository
        extends JpaRepository<ProductImage, Long> {

    // Get all images of a product
    List<ProductImage> findByProductId(Long productId);

    // Get primary image first
    List<ProductImage> findByProductIdOrderByPrimaryImageDescIdAsc(Long productId);

    // Delete all images of a product
    void deleteByProductId(Long productId);
    
}