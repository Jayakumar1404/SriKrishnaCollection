package com.example.srikrishna.Service;
import com.example.srikrishna.Entity.Order;

public interface CheckoutService {

    // =========================================================
    // PLACE ORDER
    // =========================================================

    Order placeOrder(
            Long customerId,
            String shippingAddress,
            String city,
            String state,
            String pincode,
            String phone,
            String paymentMethod
    );
}