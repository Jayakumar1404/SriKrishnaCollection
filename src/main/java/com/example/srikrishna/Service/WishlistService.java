package com.example.srikrishna.Service;

import java.util.List;
import java.util.Set;

import com.example.srikrishna.Entity.Wishlist;

public interface WishlistService {

    // =========================================================
    // GET CUSTOMER WISHLIST
    // =========================================================

    List<Wishlist> getCustomerWishlist(
            Long customerId
    );


    // =========================================================
    // GET WISHLIST PRODUCT IDs
    // Used by ProductController
    // =========================================================

    Set<Long> getWishlistProductIds(
            Long customerId
    );


    // =========================================================
    // CHECK PRODUCT IN WISHLIST
    // =========================================================

    boolean isInWishlist(
            Long customerId,
            Long productId
    );


    // =========================================================
    // ADD TO WISHLIST
    // =========================================================

    void addToWishlist(
            Long customerId,
            Long productId
    );


    // =========================================================
    // REMOVE FROM WISHLIST
    // =========================================================

    void removeFromWishlist(
            Long customerId,
            Long productId
    );


    // =========================================================
    // CLEAR CUSTOMER WISHLIST
    // =========================================================

    void clearWishlist(
            Long customerId
    );
}