package com.example.srikrishna.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.srikrishna.Entity.Customer;
import com.example.srikrishna.Entity.Order;
import com.example.srikrishna.Entity.Payment;
import com.example.srikrishna.Entity.Product;
import com.example.srikrishna.Service.CartService;
import com.example.srikrishna.Service.CheckoutService;
import com.example.srikrishna.Service.PaymentService;
import com.example.srikrishna.Service.ProductService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/Krishna")
public class CheckoutController {


    private final CartService cartService;

    private final CheckoutService checkoutService;

    private final PaymentService paymentService;

    private final ProductService productService;


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public CheckoutController(

            CartService cartService,

            CheckoutService checkoutService,

            PaymentService paymentService,

            ProductService productService) {

        this.cartService =
                cartService;

        this.checkoutService =
                checkoutService;

        this.paymentService =
                paymentService;

        this.productService =
                productService;
    }


    // =====================================================
    // CHECKOUT PAGE
    // =====================================================

    @GetMapping("/checkout")
    public String checkout(

            HttpSession session,

            Model model) {


        Customer customer =
                (Customer) session.getAttribute(
                        "loggedInCustomer"
                );


        if (customer == null) {

            return "redirect:/login";
        }


        if (customer.getStatus() == null
                || !customer.getStatus()) {

            session.invalidate();

            return "redirect:/login?error=inactive";
        }


        Long customerId =
                customer.getId();


        // =================================================
        // BUY NOW MODE
        // =================================================

        Boolean buyNowMode =
                (Boolean) session.getAttribute(
                        "buyNowMode"
                );


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

                session.removeAttribute(
                        "buyNowMode"
                );

                return "redirect:/Krishna/product";
            }


            Product product =
                    productService.getProductById(
                            productId
                    );


            if (product == null) {

                session.removeAttribute(
                        "buyNowMode"
                );

                session.removeAttribute(
                        "buyNowProductId"
                );

                session.removeAttribute(
                        "buyNowQuantity"
                );

                return "redirect:/Krishna/product";
            }


            if (quantity == null
                    || quantity < 1) {

                quantity = 1;

                session.setAttribute(
                        "buyNowQuantity",
                        quantity
                );
            }


            double buyNowTotal =
                    product.getPrice()
                            * quantity;


            model.addAttribute(
                    "buyNowMode",
                    true
            );


            model.addAttribute(
                    "buyNowProduct",
                    product
            );


            model.addAttribute(
                    "buyNowQuantity",
                    quantity
            );


            model.addAttribute(
                    "cartTotal",
                    buyNowTotal
            );


        } else {


            // =================================================
            // NORMAL CART CHECKOUT
            // =================================================

            model.addAttribute(
                    "buyNowMode",
                    false
            );


            model.addAttribute(
                    "cartItems",
                    cartService.getCustomerCart(
                            customerId
                    )
            );


            model.addAttribute(
                    "cartTotal",
                    cartService.getCartTotal(
                            customerId
                    )
            );
        }


        model.addAttribute(
                "customer",
                customer
        );


        return "customer/checkout";
    }


    // =====================================================
    // PLACE ORDER
    // =====================================================

    @PostMapping("/checkout/place-order")
    public String placeOrder(

            @RequestParam("shippingAddress")
            String shippingAddress,

            @RequestParam("city")
            String city,

            @RequestParam("state")
            String state,

            @RequestParam("pincode")
            String pincode,

            @RequestParam("phone")
            String phone,

            @RequestParam(
                    value = "paymentMethod",
                    defaultValue = "COD")
            String paymentMethod,

            HttpSession session,

            RedirectAttributes redirectAttributes) {


        Customer customer =
                (Customer) session.getAttribute(
                        "loggedInCustomer"
                );


        if (customer == null) {

            return "redirect:/login";
        }


        if (customer.getStatus() == null
                || !customer.getStatus()) {

            session.invalidate();

            return "redirect:/login?error=inactive";
        }


        try {


            // =================================================
            // BUY NOW MODE
            // =================================================

            Boolean buyNowMode =
                    (Boolean) session.getAttribute(
                            "buyNowMode"
                    );


            // =================================================
            // ONLINE PAYMENT
            // =================================================

            if ("ONLINE".equalsIgnoreCase(
                    paymentMethod)) {


                session.setAttribute(
                        "pendingShippingAddress",
                        shippingAddress
                );


                session.setAttribute(
                        "pendingCity",
                        city
                );


                session.setAttribute(
                        "pendingState",
                        state
                );


                session.setAttribute(
                        "pendingPincode",
                        pincode
                );


                session.setAttribute(
                        "pendingPhone",
                        phone
                );


                return "redirect:/Krishna/payment";
            }


            // =================================================
            // COD
            // =================================================

            Order order;


            if (Boolean.TRUE.equals(
                    buyNowMode)) {


                /*
                 * Buy Now order.
                 *
                 * We use the existing CheckoutService
                 * for the final order creation.
                 *
                 * The Buy Now product must be handled
                 * by your existing order/cart implementation.
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

                    throw new RuntimeException(
                            "Buy Now session expired."
                    );
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


                if (quantity == null
                        || quantity < 1) {

                    quantity = 1;
                }


                /*
                 * For the current project architecture,
                 * place the product into the cart first,
                 * then use the existing CheckoutService.
                 */

                cartService.addToCart(
                        customer.getId(),
                        productId,
                        quantity
                );


                order =
                        checkoutService.placeOrder(

                                customer.getId(),

                                shippingAddress,

                                city,

                                state,

                                pincode,

                                phone,

                                "COD"
                        );


            } else {


                // =================================================
                // NORMAL CART COD
                // =================================================

                order =
                        checkoutService.placeOrder(

                                customer.getId(),

                                shippingAddress,

                                city,

                                state,

                                pincode,

                                phone,

                                "COD"
                        );
            }


            // =================================================
            // CREATE COD PAYMENT
            // =================================================

            Payment payment =
                    new Payment();


            payment.setOrderId(
                    order.getId()
            );


            payment.setPaymentMethod(
                    "COD"
            );


            payment.setAmount(
                    order.getTotalAmount()
            );


            payment.setPaymentStatus(
                    "PENDING"
            );


            payment.setTransactionId(
                    null
            );


            paymentService.save(
                    payment
            );


            // =================================================
            // CLEAR BUY NOW SESSION
            // =================================================

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
            // SUCCESS
            // =================================================

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Order placed successfully."
            );


            redirectAttributes.addFlashAttribute(
                    "order",
                    order
            );


            return "redirect:/Krishna/order-success";


        } catch (RuntimeException e) {


            e.printStackTrace();


            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage()
            );


            return "redirect:/Krishna/checkout";
        }
    }


    // =====================================================
    // ORDER SUCCESS
    // =====================================================

  @GetMapping("/order-success")
public String orderSuccess(
        HttpSession session,
        Model model) {

    Customer customer =
            (Customer) session.getAttribute(
                    "loggedInCustomer"
            );

    if (customer == null) {

        return "redirect:/login";
    }


    // =====================================================
    // FLASH ORDER
    // =====================================================

    if (model.containsAttribute("order")) {

        model.addAttribute(
                "customer",
                customer
        );

        return "customer/order-success";
    }


    // =====================================================
    // RAZORPAY LAST ORDER
    // =====================================================

    Object lastOrderId =
            session.getAttribute(
                    "lastOrderId"
            );

    if (lastOrderId != null) {

        model.addAttribute(
                "orderId",
                lastOrderId
        );

        model.addAttribute(
                "customer",
                customer
        );

        session.removeAttribute(
                "lastOrderId"
        );

        return "customer/order-success";
    }


    return "redirect:/Krishna/orders";
}
}