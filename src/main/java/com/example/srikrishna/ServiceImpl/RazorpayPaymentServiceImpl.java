package com.example.srikrishna.ServiceImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.srikrishna.Entity.Product;
import com.example.srikrishna.Repository.CartRepository;
import com.example.srikrishna.Service.CartService;
import com.example.srikrishna.Service.ProductService;
import com.example.srikrishna.Service.RazorpayPaymentService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;

import jakarta.servlet.http.HttpSession;

@Service
public class RazorpayPaymentServiceImpl
        implements RazorpayPaymentService {


    private final RazorpayClient razorpayClient;

    private final CartService cartService;

    private final CartRepository cartRepository;

    private final ProductService productService;


    @Value("${razorpay.key.id}")
    private String keyId;


    @Value("${razorpay.key.secret}")
    private String keySecret;


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public RazorpayPaymentServiceImpl(

            RazorpayClient razorpayClient,

            CartService cartService,

            CartRepository cartRepository,

            ProductService productService) {

        this.razorpayClient =
                razorpayClient;

        this.cartService =
                cartService;

        this.cartRepository =
                cartRepository;

        this.productService =
                productService;
    }


    // =====================================================
    // CREATE RAZORPAY ORDER
    // =====================================================

    @Override
    public Map<String, Object> createOrder(

            Long customerId,

            HttpSession session) throws Exception {


        // =================================================
        // CHECK BUY NOW
        // =================================================

        Boolean buyNowMode =
                (Boolean) session.getAttribute(
                        "buyNowMode"
                );


        double total;


        // =================================================
        // BUY NOW TOTAL
        // =================================================

        if (Boolean.TRUE.equals(
                buyNowMode)) {


            Long productId =
                    (Long) session.getAttribute(
                            "buyNowProductId"
                    );


            Integer quantity =
                    (Integer) session.getAttribute(
                            "buyNowQuantity"
                    );


            if (productId == null) {

                throw new RuntimeException(
                        "Buy Now session expired."
                );
            }


            if (quantity == null
                    || quantity < 1) {

                quantity = 1;
            }


            Product product =
                    productService.getProductById(
                            productId
                    );


            if (product == null) {

                throw new RuntimeException(
                        "Product not found."
                );
            }


            if (product.getStatus() == null
                    || !product.getStatus()) {

                throw new RuntimeException(
                        "Product is inactive."
                );
            }


            if (product.getStock() == null
                    || product.getStock() < quantity) {

                throw new RuntimeException(
                        "Insufficient product stock."
                );
            }


            total =
                    product.getPrice()
                            * quantity;


        } else {


            // =================================================
            // NORMAL CART TOTAL
            // =================================================

            if (cartRepository
                    .findByCustomerId(customerId)
                    .isEmpty()) {

                throw new RuntimeException(
                        "Your cart is empty."
                );
            }


            Double cartTotal =
                    cartService.getCartTotal(
                            customerId
                    );


            if (cartTotal == null
                    || cartTotal <= 0) {

                throw new RuntimeException(
                        "Invalid cart amount."
                );
            }


            total =
                    cartTotal;
        }


        // =================================================
        // RUPEES → PAISE
        // =================================================

        long amountInPaise =
                Math.round(
                        total * 100
                );


        // =================================================
        // RECEIPT
        // =================================================

        String receipt =
                "SKC_"
                + customerId
                + "_"
                + UUID.randomUUID()
                        .toString()
                        .substring(
                                0,
                                8
                        );


        // =================================================
        // RAZORPAY OPTIONS
        // =================================================

        JSONObject options =
                new JSONObject();


        options.put(
                "amount",
                amountInPaise
        );


        options.put(
                "currency",
                "INR"
        );


        options.put(
                "receipt",
                receipt
        );


        // =================================================
        // CREATE RAZORPAY ORDER
        // =================================================

        Order razorpayOrder =
                razorpayClient.orders.create(
                        options
                );


        // =================================================
        // SAVE SERVER ORDER ID
        // =================================================

        session.setAttribute(
                "pendingRazorpayOrderId",
                razorpayOrder.get("id")
        );


        // =================================================
        // RESPONSE
        // =================================================

        Map<String, Object> response =
                new HashMap<>();


        response.put(
                "success",
                true
        );


        response.put(
                "keyId",
                keyId
        );


        response.put(
                "orderId",
                razorpayOrder.get("id")
        );


        response.put(
                "amount",
                amountInPaise
        );


        response.put(
                "currency",
                "INR"
        );


        response.put(
                "receipt",
                receipt
        );


        return response;
    }


    // =====================================================
    // VERIFY PAYMENT
    // =====================================================

    @Override
    public boolean verifyPayment(

            String razorpayPaymentId,

            String razorpaySignature,

            String serverRazorpayOrderId) {


        try {


            JSONObject options =
                    new JSONObject();


            options.put(
                    "razorpay_order_id",
                    serverRazorpayOrderId
            );


            options.put(
                    "razorpay_payment_id",
                    razorpayPaymentId
            );


            options.put(
                    "razorpay_signature",
                    razorpaySignature
            );


            return Utils.verifyPaymentSignature(

                    options,

                    keySecret
            );


        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }
}