package com.example.srikrishna.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.srikrishna.Entity.Customer;
import com.example.srikrishna.Service.WishlistService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/Krishna")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(
            WishlistService wishlistService) {

        this.wishlistService = wishlistService;
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
    // WISHLIST PAGE
    // =========================================================

    @GetMapping("/wishlist")
    public String wishlist(
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
                "wishlistItems",
                wishlistService.getCustomerWishlist(
                        customerId)
        );

        model.addAttribute(
                "customer",
                customer
        );

        model.addAttribute(
                "customerId",
                customerId
        );


        return "customer/wishlist";
    }


    // =========================================================
    // TOGGLE WISHLIST
    //
    // ADD  -> if product is not in wishlist
    // REMOVE -> if product is already in wishlist
    // =========================================================

    @PostMapping("/wishlist/toggle")
    public String toggleWishlist(

            @org.springframework.web.bind.annotation.RequestParam(
                    "productId")
            Long productId,

            HttpSession session,

            RedirectAttributes redirectAttributes) {


        // -----------------------------------------------------
        // CHECK LOGIN
        // -----------------------------------------------------

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

            // -------------------------------------------------
            // CHECK WHETHER PRODUCT IS ALREADY IN WISHLIST
            // -------------------------------------------------

            boolean alreadyInWishlist =
                    wishlistService.isInWishlist(
                            customerId,
                            productId);


            // -------------------------------------------------
            // REMOVE
            // -------------------------------------------------

            if (alreadyInWishlist) {

                wishlistService.removeFromWishlist(
                        customerId,
                        productId);

                redirectAttributes.addFlashAttribute(
                        "success",
                        "Product removed from wishlist!"
                );

            }


            // -------------------------------------------------
            // ADD
            // -------------------------------------------------

            else {

                wishlistService.addToWishlist(
                        customerId,
                        productId);

                redirectAttributes.addFlashAttribute(
                        "success",
                        "Product added to wishlist!"
                );

            }

        } catch (Exception e) {

            e.printStackTrace();

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage()
            );
        }


        // -----------------------------------------------------
        // RETURN TO PRODUCT PAGE
        // -----------------------------------------------------

        return "redirect:/Krishna/product";
    }


    // =========================================================
    // ADD TO WISHLIST
    // =========================================================

    @PostMapping("/wishlist/add/{productId}")
    public String addToWishlist(

            @PathVariable Long productId,

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

            wishlistService.addToWishlist(
                    customerId,
                    productId
            );

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Product added to wishlist!"
            );

        } catch (Exception e) {

            e.printStackTrace();

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage()
            );
        }


        return "redirect:/Krishna/wishlist";
    }


    // =========================================================
    // REMOVE FROM WISHLIST
    // =========================================================

    @GetMapping("/wishlist/remove/{productId}")
    public String removeFromWishlist(

            @PathVariable Long productId,

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

            wishlistService.removeFromWishlist(
                    customerId,
                    productId
            );

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Product removed from wishlist!"
            );

        } catch (Exception e) {

            e.printStackTrace();

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Unable to remove product from wishlist."
            );
        }


        return "redirect:/Krishna/wishlist";
    }


    // =========================================================
    // CLEAR WISHLIST
    // =========================================================

    @GetMapping("/wishlist/clear")
    public String clearWishlist(

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

            wishlistService.clearWishlist(
                    customerId
            );

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Wishlist cleared successfully!"
            );

        } catch (Exception e) {

            e.printStackTrace();

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Unable to clear wishlist."
            );
        }


        return "redirect:/Krishna/wishlist";
    }
}