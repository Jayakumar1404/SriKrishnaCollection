package com.example.srikrishna.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/Krishna")
public class AuthController {

    /*
     * =========================================================
     * OLD AUTH ROUTES
     * =========================================================
     *
     * This controller is kept temporarily so that old links
     * using /Krishna/... do not break.
     *
     * The actual customer authentication is now handled by:
     *
     * CustomerAuthController
     *
     * Active customer login:
     *
     * /login
     *
     * =========================================================
     */


    // =========================================================
    // OLD CUSTOMER LOGIN
    // =========================================================

    @GetMapping("/login")
    public String login() {

        return "redirect:/login";
    }


    // =========================================================
    // OLD CUSTOMER REGISTER
    // =========================================================

    @GetMapping("/register")
    public String register() {

        return "redirect:/register";
    }


    // =========================================================
    // OLD FORGOT PASSWORD
    // =========================================================

    @GetMapping("/forgot-password")
    public String forgotPassword() {

        return "redirect:/forgot-password";
    }


    // =========================================================
    // OLD OTP
    // =========================================================

    @GetMapping("/otp")
    public String otp() {

        return "redirect:/login";
    }


    // =========================================================
    // OLD RESET PASSWORD
    // =========================================================

    @GetMapping("/reset-password")
    public String resetPassword() {

        return "redirect:/reset-password";
    }


    // =========================================================
    // OLD LOGOUT
    // =========================================================

    @GetMapping("/logout")
    public String logout() {

        return "redirect:/login";
    }
}