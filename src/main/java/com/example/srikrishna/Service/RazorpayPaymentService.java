package com.example.srikrishna.Service;

import java.util.Map;

import jakarta.servlet.http.HttpSession;

public interface RazorpayPaymentService {

    Map<String, Object> createOrder(
            Long customerId,
            HttpSession session
    ) throws Exception;


    boolean verifyPayment(

            String razorpayPaymentId,

            String razorpaySignature,

            String serverRazorpayOrderId
    );
}