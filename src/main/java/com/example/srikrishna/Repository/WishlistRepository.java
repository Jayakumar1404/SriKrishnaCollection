package com.example.srikrishna.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.srikrishna.Entity.Wishlist;

@Repository
public interface WishlistRepository
        extends JpaRepository<Wishlist, Long> {

    // =========================================================
    // GET CUSTOMER WISHLIST
    // =========================================================

    List<Wishlist> findByCustomerId(
            Long customerId
    );


    // =========================================================
    // FIND CUSTOMER + PRODUCT
    // =========================================================

    Wishlist findByCustomerIdAndProductId(
            Long customerId,
            Long productId
    );


    // =========================================================
    // DELETE CUSTOMER WISHLIST
    // =========================================================

    void deleteByCustomerId(
            Long customerId
    );
}