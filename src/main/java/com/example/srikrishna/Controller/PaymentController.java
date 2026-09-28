package com.example.srikrishna.Controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.srikrishna.Entity.Customer;
import com.example.srikrishna.Entity.Order;
import com.example.srikrishna.Entity.Payment;
import com.example.srikrishna.Repository.OrderRepository;
import com.example.srikrishna.Service.CheckoutService;
import com.example.srikrishna.Service.PaymentService;
import com.example.srikrishna.Service.RazorpayPaymentService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/Krishna")
public class PaymentController {


    private final RazorpayPaymentService razorpayPaymentService;

    private final CheckoutService checkoutService;

    private final PaymentService paymentService;

    private final OrderRepository orderRepository;


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public PaymentController(

            RazorpayPaymentService razorpayPaymentService,

            CheckoutService checkoutService,

            PaymentService paymentService,

            OrderRepository orderRepository) {

        this.razorpayPaymentService =
                razorpayPaymentService;

        this.checkoutService =
                checkoutService;

        this.paymentService =
                paymentService;

        this.orderRepository =
                orderRepository;
    }


    // =====================================================
    // PAYMENT PAGE
    // =====================================================

    @GetMapping("/payment")
    public String payment(
            HttpSession session,
            org.springframework.ui.Model model) {


        Customer customer =
                (Customer) session.getAttribute(
                        "loggedInCustomer"
                );


        if (customer == null) {

            return "redirect:/login";
        }


        model.addAttribute(
                "customer",
                customer
        );


        return "customer/payment";
    }


    // =====================================================
    // CREATE RAZORPAY ORDER
    // =====================================================

    @PostMapping("/payment/create-order")
    public ResponseEntity<?> createOrder(
            HttpSession session) {


        try {


            Customer customer =
                    (Customer) session.getAttribute(
                            "loggedInCustomer"
                    );


            if (customer == null) {

                return ResponseEntity
                        .status(401)
                        .body(
                                Map.of(
                                        "success",
                                        false,
                                        "message",
                                        "Please login."
                                )
                        );
            }


            Map<String, Object> response =
                    razorpayPaymentService
                            .createOrder(
                                    customer.getId(),
                                    session
                            );


            return ResponseEntity.ok(
                    response
            );


        } catch (Exception e) {


            e.printStackTrace();


            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "success",
                                    false,
                                    "message",
                                    e.getMessage()
                            )
                    );
        }
    }


    // =====================================================
    // VERIFY RAZORPAY PAYMENT
    // =====================================================

    @PostMapping("/payment/verify")
    public ResponseEntity<?> verifyPayment(

            @RequestBody Map<String, String> data,

            HttpSession session) {


        try {


            Customer customer =
                    (Customer) session.getAttribute(
                            "loggedInCustomer"
                    );


            if (customer == null) {

                return ResponseEntity
                        .status(401)
                        .body(
                                Map.of(
                                        "success",
                                        false,
                                        "message",
                                        "Please login."
                                )
                        );
            }


            // =================================================
            // SERVER RAZORPAY ORDER
            // =================================================

            Object pendingOrder =
                    session.getAttribute(
                            "pendingRazorpayOrderId"
                    );


            if (pendingOrder == null) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                Map.of(
                                        "success",
                                        false,
                                        "message",
                                        "Payment session expired."
                                )
                        );
            }


            String serverOrderId =
                    pendingOrder.toString();


            // =================================================
            // PAYMENT RESPONSE
            // =================================================

            String paymentId =
                    data.get(
                            "razorpay_payment_id"
                    );


            String signature =
                    data.get(
                            "razorpay_signature"
                    );


            if (paymentId == null
                    || signature == null) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                Map.of(
                                        "success",
                                        false,
                                        "message",
                                        "Invalid Razorpay response."
                                )
                        );
            }


            // =================================================
            // VERIFY
            // =================================================

            boolean verified =
                    razorpayPaymentService
                            .verifyPayment(

                                    paymentId,

                                    signature,

                                    serverOrderId
                            );


            if (!verified) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                Map.of(
                                        "success",
                                        false,
                                        "message",
                                        "Payment verification failed."
                                )
                        );
            }


            // =================================================
            // CHECK DUPLICATE
            // =================================================

            if (paymentService
                    .paymentExists(paymentId)) {

                return ResponseEntity.ok(
                        Map.of(
                                "success",
                                true,
                                "message",
                                "Payment already processed."
                        )
                );
            }


            // =================================================
            // SHIPPING DETAILS
            // =================================================

            String shippingAddress =
                    (String) session.getAttribute(
                            "pendingShippingAddress"
                    );


            String city =
                    (String) session.getAttribute(
                            "pendingCity"
                    );


            String state =
                    (String) session.getAttribute(
                            "pendingState"
                    );


            String pincode =
                    (String) session.getAttribute(
                            "pendingPincode"
                    );


            String phone =
                    (String) session.getAttribute(
                            "pendingPhone"
                    );


            if (shippingAddress == null
                    || city == null
                    || state == null
                    || pincode == null
                    || phone == null) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                Map.of(
                                        "success",
                                        false,
                                        "message",
                                        "Checkout session expired."
                                )
                        );
            }


            // =================================================
            // BUY NOW / CART
            // =================================================

            Boolean buyNowMode =
                    (Boolean) session.getAttribute(
                            "buyNowMode"
                    );


            Order order;


            if (Boolean.TRUE.equals(
                    buyNowMode)) {


                /*
                 * Add Buy Now product to cart,
                 * then use the existing CheckoutService.
                 */

                Long productId =
                        (Long) session.getAttribute(
                                "buyNowProductId"
                        );


                Integer quantity =
                        (Integer) session.getAttribute(
                                "buyNowQuantity"
                        );


                if (productId == null) {

                    return ResponseEntity
                            .badRequest()
                            .body(
                                    Map.of(
                                            "success",
                                            false,
                                            "message",
                                            "Buy Now session expired."
                                    )
                            );
                }


                if (quantity == null
                        || quantity < 1) {

                    quantity = 1;
                }


                /*
                 * IMPORTANT:
                 * This line depends on your CartService.
                 */

                // cartService.addToCart(...)


                /*
                 * Your existing CheckoutService can now
                 * create the order from the cart.
                 *
                 * Add the exact CartService call here after
                 * checking your CartService method.
                 */


                order =
                        checkoutService.placeOrder(

                                customer.getId(),

                                shippingAddress,

                                city,

                                state,

                                pincode,

                                phone,

                                "RAZORPAY"
                        );


            } else {


                // =================================================
                // NORMAL CART
                // =================================================

                order =
                        checkoutService.placeOrder(

                                customer.getId(),

                                shippingAddress,

                                city,

                                state,

                                pincode,

                                phone,

                                "RAZORPAY"
                        );
            }


            // =================================================
            // ORDER PAYMENT STATUS
            // =================================================

            order.setPaymentStatus(
                    "PAID"
            );


            orderRepository.save(
                    order
            );


            // =================================================
            // PAYMENT RECORD
            // =================================================

            Payment payment =
                    new Payment();


            payment.setOrderId(
                    order.getId()
            );


            payment.setPaymentMethod(
                    "RAZORPAY"
            );


            payment.setAmount(
                    order.getTotalAmount()
            );


            payment.setPaymentStatus(
                    "SUCCESS"
            );


            payment.setRazorpayOrderId(
                    serverOrderId
            );


            payment.setRazorpayPaymentId(
                    paymentId
            );


            payment.setRazorpaySignature(
                    signature
            );


            payment.setTransactionId(
                    paymentId
            );


            paymentService.save(
                    payment
            );


            // =================================================
            // CLEAR PAYMENT SESSION
            // =================================================

            session.removeAttribute(
                    "pendingRazorpayOrderId"
            );


            session.removeAttribute(
                    "pendingShippingAddress"
            );


            session.removeAttribute(
                    "pendingCity"
            );


            session.removeAttribute(
                    "pendingState"
            );


            session.removeAttribute(
                    "pendingPincode"
            );


            session.removeAttribute(
                    "pendingPhone"
            );


            session.removeAttribute(
                    "buyNowMode"
            );


            session.removeAttribute(
                    "buyNowProductId"
            );


            session.removeAttribute(
                    "buyNowQuantity"
            );


            // =================================================
            // SAVE SUCCESS ORDER
            // =================================================

            session.setAttribute(
                    "lastOrderId",
                    order.getId()
            );


            return ResponseEntity.ok(
                    Map.of(
                            "success",
                            true,
                            "orderId",
                            order.getId()
                    )
            );


        } catch (Exception e) {


            e.printStackTrace();


            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "success",
                                    false,
                                    "message",
                                    e.getMessage()
                            )
                    );
        }
    }
}