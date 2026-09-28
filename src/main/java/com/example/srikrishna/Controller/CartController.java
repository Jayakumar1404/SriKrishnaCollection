package com.example.srikrishna.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.srikrishna.Entity.Customer;
import com.example.srikrishna.Service.CartService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/Krishna")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }


    // =========================================================
    // GET LOGGED-IN CUSTOMER
    // =========================================================

    private Customer getLoggedInCustomer(
            HttpSession session) {

        return (Customer) session.getAttribute(
                "loggedInCustomer");
    }


    // =========================================================
    // CUSTOMER AUTHENTICATION CHECK
    // =========================================================

    private String checkCustomer(
            HttpSession session) {

        Customer customer =
                getLoggedInCustomer(session);

        if (customer == null) {

            return "redirect:/login";
        }

        if (customer.getStatus() == null
                || !customer.getStatus()) {

            session.invalidate();

            return "redirect:/login?error=inactive";
        }

        return null;
    }


    // =========================================================
    // CART PAGE
    // =========================================================

    @GetMapping("/cart")
    public String cart(
            HttpSession session,
            Model model) {

        String authenticationCheck =
                checkCustomer(session);

        if (authenticationCheck != null) {

            return authenticationCheck;
        }

        Customer customer =
                getLoggedInCustomer(session);

        Long customerId =
                customer.getId();


        model.addAttribute(
                "cartItems",
                cartService.getCustomerCart(customerId)
        );

        model.addAttribute(
                "cartTotal",
                cartService.getCartTotal(customerId)
        );

        model.addAttribute(
                "customerId",
                customerId
        );

        model.addAttribute(
                "customer",
                customer
        );


        return "customer/cart";
    }


    // =========================================================
    // ADD TO CART
    // =========================================================

    @PostMapping("/cart/add")
    public String addToCart(

            @RequestParam Long productId,

            @RequestParam(defaultValue = "1")
            int quantity,

            HttpSession session,

            RedirectAttributes redirectAttributes) {

        String authenticationCheck =
                checkCustomer(session);

        if (authenticationCheck != null) {

            return authenticationCheck;
        }

        Customer customer =
                getLoggedInCustomer(session);

        Long customerId =
                customer.getId();


        try {

            if (quantity <= 0) {

                redirectAttributes.addFlashAttribute(
                        "error",
                        "Quantity must be greater than zero."
                );

                return "redirect:/Krishna/cart";
            }


            cartService.addToCart(
                    customerId,
                    productId,
                    quantity
            );


            redirectAttributes.addFlashAttribute(
                    "success",
                    "Product added to cart successfully!"
            );

        } catch (Exception e) {

            e.printStackTrace();

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage()
            );
        }


        return "redirect:/Krishna/cart";
    }


    // =========================================================
    // UPDATE QUANTITY
    // =========================================================

    @PostMapping("/cart/update")
    public String updateQuantity(

            @RequestParam Long cartId,

            @RequestParam int quantity,

            HttpSession session,

            RedirectAttributes redirectAttributes) {

        String authenticationCheck =
                checkCustomer(session);

        if (authenticationCheck != null) {

            return authenticationCheck;
        }

        Customer customer =
                getLoggedInCustomer(session);

        Long customerId =
                customer.getId();


        try {

            if (quantity <= 0) {

                redirectAttributes.addFlashAttribute(
                        "error",
                        "Quantity must be greater than zero."
                );

                return "redirect:/Krishna/cart";
            }


            cartService.updateQuantity(
                    customerId,
                    cartId,
                    quantity
            );


            redirectAttributes.addFlashAttribute(
                    "success",
                    "Cart updated successfully!"
            );

        } catch (Exception e) {

            e.printStackTrace();

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage()
            );
        }


        return "redirect:/Krishna/cart";
    }


    // =========================================================
    // REMOVE FROM CART
    // =========================================================

    @GetMapping("/cart/remove/{cartId}")
    public String removeFromCart(

            @PathVariable Long cartId,

            HttpSession session,

            RedirectAttributes redirectAttributes) {

        String authenticationCheck =
                checkCustomer(session);

        if (authenticationCheck != null) {

            return authenticationCheck;
        }

        Customer customer =
                getLoggedInCustomer(session);

        Long customerId =
                customer.getId();


        try {

            cartService.removeFromCart(
                    customerId,
                    cartId
            );


            redirectAttributes.addFlashAttribute(
                    "success",
                    "Product removed from cart!"
            );

        } catch (Exception e) {

            e.printStackTrace();

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage()
            );
        }


        return "redirect:/Krishna/cart";
    }


    // =========================================================
    // CLEAR CART
    // =========================================================

    @GetMapping("/cart/clear")
    public String clearCart(

            HttpSession session,

            RedirectAttributes redirectAttributes) {

        String authenticationCheck =
                checkCustomer(session);

        if (authenticationCheck != null) {

            return authenticationCheck;
        }

        Customer customer =
                getLoggedInCustomer(session);

        Long customerId =
                customer.getId();


        try {

            cartService.clearCart(
                    customerId
            );


            redirectAttributes.addFlashAttribute(
                    "success",
                    "Cart cleared successfully!"
            );

        } catch (Exception e) {

            e.printStackTrace();

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Unable to clear cart."
            );
        }


        return "redirect:/Krishna/cart";
    }
}