package com.example.srikrishna.Service;

import java.util.List;

import com.example.srikrishna.Entity.Cart;

public interface CartService {

    // =========================================================
    // GET CUSTOMER CART
    // =========================================================

    List<Cart> getCustomerCart(Long customerId);


    // =========================================================
    // CART TOTAL
    // =========================================================

    Double getCartTotal(Long customerId);


    // =========================================================
    // ADD TO CART
    // =========================================================

    void addToCart(
            Long customerId,
            Long productId,
            int quantity
    );


    // =========================================================
    // UPDATE QUANTITY
    // =========================================================

    void updateQuantity(
            Long customerId,
            Long cartId,
            int quantity
    );


    // =========================================================
    // REMOVE CART ITEM
    // =========================================================

    void removeFromCart(
            Long customerId,
            Long cartId
    );


    // =========================================================
    // CLEAR CUSTOMER CART
    // =========================================================

    void clearCart(Long customerId);
}