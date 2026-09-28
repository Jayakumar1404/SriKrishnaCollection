package com.example.srikrishna.Controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.srikrishna.Entity.Customer;
import com.example.srikrishna.Entity.PasswordReset;
import com.example.srikrishna.Entity.Product;
import com.example.srikrishna.Repository.PasswordResetRepository;
import com.example.srikrishna.Service.CategoryService;
import com.example.srikrishna.Service.CustomerService;
import com.example.srikrishna.Service.PasswordResetService;
import com.example.srikrishna.Service.ProductService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class CustomerAuthController {

        @Autowired
        private PasswordResetService passwordResetService;

        @Autowired
        private PasswordResetRepository passwordResetRepository;

        @Autowired
        private CustomerService customerService;

        @Autowired
        private JavaMailSender mailSender;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Autowired
        private CategoryService categoryService;

        @Autowired
        private ProductService productService;
        // =========================================================
        // CUSTOMER REGISTRATION PAGE
        // =========================================================

        @GetMapping("/register")
        public String registerPage(Model model) {

                model.addAttribute("customer", new Customer());

                return "customer/register";
        }

        // =========================================================
        // CUSTOMER REGISTRATION
        // =========================================================

        @PostMapping("/register")
        public String registerCustomer(
                        @ModelAttribute("customer") Customer customer,
                        Model model) {

                try {

                        customerService.saveCustomer(customer);

                        return "redirect:/login?register=success";

                } catch (RuntimeException e) {

                        model.addAttribute(
                                        "error",
                                        e.getMessage());

                        model.addAttribute(
                                        "customer",
                                        customer);

                        return "customer/register";
                }
        }

        // =========================================================
        // CUSTOMER LOGIN PAGE
        // =========================================================

        @GetMapping("/login")
        public String loginPage() {

                return "customer/login";
        }

        // =========================================================
        // CUSTOMER LOGIN
        // =========================================================

        @PostMapping("/login")
        public String loginCustomer(
                        @RequestParam("email") String email,
                        @RequestParam("password") String password,
                        HttpServletRequest request,
                        Model model) {

                // ---------------------------------------------------------
                // Find customer
                // ---------------------------------------------------------

                Customer customer = customerService.getCustomerByEmail(email);
                System.out.println("========== CUSTOMER LOGIN DEBUG ==========");
                System.out.println("Email entered    : " + email);
                System.out.println("Customer found   : " + (customer != null));

                if (customer != null) {
                        System.out.println("Customer email   : " + customer.getEmail());
                        System.out.println("Customer status  : " + customer.getStatus());
                        System.out.println("Password exists  : " + (customer.getPassword() != null));

                        if (customer.getPassword() != null) {
                                System.out.println(
                                                "BCrypt matches   : "
                                                                + passwordEncoder.matches(
                                                                                password,
                                                                                customer.getPassword()));
                        }
                }

                System.out.println("==========================================");

                if (customer == null) {

                        model.addAttribute(
                                        "error",
                                        "Invalid email or password");

                        return "customer/login";
                }

                // ---------------------------------------------------------
                // Check account status
                // ---------------------------------------------------------

                if (customer.getStatus() == null
                                || !customer.getStatus()) {

                        model.addAttribute(
                                        "error",
                                        "Your account is inactive");

                        return "customer/login";
                }

                // ---------------------------------------------------------
                // BCrypt password verification
                // ---------------------------------------------------------

                if (customer.getPassword() == null
                                || !passwordEncoder.matches(
                                                password,
                                                customer.getPassword())) {

                        model.addAttribute(
                                        "error",
                                        "Invalid email or password");

                        return "customer/login";
                }

                // ---------------------------------------------------------
                // Session fixation protection
                // ---------------------------------------------------------

                HttpSession oldSession = request.getSession(false);

                if (oldSession != null) {
                        oldSession.invalidate();
                }

                HttpSession session = request.getSession(true);

                // ---------------------------------------------------------
                // Store logged-in customer
                // ---------------------------------------------------------

                session.setAttribute(
                                "loggedInCustomer",
                                customer);

                session.setAttribute(
                                "customerAuthenticated",
                                true);

                // ---------------------------------------------------------
                // Redirect customer home
                // ---------------------------------------------------------

                return "redirect:/";
        }

        // =========================================================
        // CUSTOMER HOME
        // =========================================================
@GetMapping("/")
public String home(Model model) {

    System.out.println("================================");
    System.out.println("        HOME CONTROLLER HIT");
    System.out.println("================================");

    List<Product> products =
            productService.getAllProducts();

    List<com.example.srikrishna.Entity.Category> categories =
            categoryService.getAllCategories();

    System.out.println(
            "PRODUCT COUNT FROM DB = "
            + products.size()
    );

    System.out.println(
            "CATEGORY COUNT FROM DB = "
            + categories.size()
    );

    for (com.example.srikrishna.Entity.Category category : categories) {

        System.out.println(
                "CATEGORY = "
                + category.getId()
                + " | "
                + category.getName()
                + " | STATUS = "
                + category.getStatus()
        );
    }

    model.addAttribute(
            "products",
            products
    );

    model.addAttribute(
            "categories",
            categories
    );

    return "customer/home";
}
        // @GetMapping("/")
        // public String home(Model model) {

        //         // ==============================
        //         // PRODUCTS
        //         // ==============================

        //         List<Product> products = productService.getAllProducts();

        //         model.addAttribute(
        //                         "products",
        //                         products);

        //         // ==============================
        //         // CATEGORIES
        //         // ==============================

        //         model.addAttribute(
        //                         "categories",
        //                         categoryService.getAllCategories());

        //         return "customer/home";
        // }
        // =========================================================
        // CUSTOMER LOGOUT
        // =========================================================

        @GetMapping("/logout")
        public String logout(HttpSession session) {

                if (session != null) {
                        session.invalidate();
                }

                return "redirect:/login?logout=success";
        }

        // =========================================================
        // FORGOT PASSWORD PAGE
        // =========================================================

        @GetMapping("/forgot-password")
        public String forgotPasswordPage() {

                return "customer/forgot-password";
        }

        // =========================================================
        // SEND OTP
        // =========================================================

        @Transactional
        @PostMapping("/forgot-password")
        public String forgotPassword(
                        @RequestParam("email") String email,
                        HttpSession session,
                        Model model) {

                Customer customer = customerService.getCustomerByEmail(email);

                if (customer == null) {

                        model.addAttribute(
                                        "error",
                                        "Email address not registered");

                        return "customer/forgot-password";
                }

                // ---------------------------------------------------------
                // Generate 6-digit OTP
                // ---------------------------------------------------------

                String otp = String.format(
                                "%06d",
                                new Random().nextInt(1_000_000));

                // ---------------------------------------------------------
                // Remove previous OTP
                // ---------------------------------------------------------

                passwordResetRepository.deleteByEmail(email);

                // ---------------------------------------------------------
                // Create new password reset record
                // ---------------------------------------------------------

                PasswordReset reset = new PasswordReset();

                reset.setEmail(email);

                reset.setOtp(otp);

                reset.setExpiryTime(
                                LocalDateTime.now()
                                                .plusMinutes(5));

                passwordResetRepository.save(reset);

                // ---------------------------------------------------------
                // Store reset email in session
                // ---------------------------------------------------------

                session.setAttribute(
                                "passwordResetEmail",
                                email);

                session.setAttribute(
                                "otpVerified",
                                false);

                // ---------------------------------------------------------
                // Send OTP email
                // ---------------------------------------------------------

                SimpleMailMessage message = new SimpleMailMessage();

                message.setTo(email);

                message.setSubject(
                                "Sri Krishna Collection - Password Reset OTP");

                message.setText(
                                "Your password reset OTP is: "
                                                + otp
                                                + "\n\n"
                                                + "This OTP is valid for 5 minutes."
                                                + "\n\n"
                                                + "If you did not request a password reset, "
                                                + "please ignore this email.");

                mailSender.send(message);

                model.addAttribute(
                                "success",
                                "OTP has been sent to your email.");

                model.addAttribute(
                                "email",
                                email);

                return "customer/verify-otp";
        }

        // =========================================================
        // VERIFY OTP
        // =========================================================

        @PostMapping("/verify-otp")
        public String verifyOtp(
                        @RequestParam("email") String email,
                        @RequestParam("otp") String otp,
                        HttpSession session,
                        Model model) {

                PasswordReset reset = passwordResetRepository
                                .findByEmail(email)
                                .orElse(null);

                // ---------------------------------------------------------
                // OTP not found
                // ---------------------------------------------------------

                if (reset == null) {

                        model.addAttribute(
                                        "error",
                                        "OTP not found. Please request a new OTP.");

                        model.addAttribute(
                                        "email",
                                        email);

                        return "customer/verify-otp";
                }

                // ---------------------------------------------------------
                // OTP expired
                // ---------------------------------------------------------

                if (reset.getExpiryTime()
                                .isBefore(LocalDateTime.now())) {

                        passwordResetRepository.delete(reset);

                        model.addAttribute(
                                        "error",
                                        "OTP has expired. Please request a new OTP.");

                        model.addAttribute(
                                        "email",
                                        email);

                        return "customer/verify-otp";
                }

                // ---------------------------------------------------------
                // OTP mismatch
                // ---------------------------------------------------------

                if (!reset.getOtp().equals(otp)) {

                        model.addAttribute(
                                        "error",
                                        "Invalid OTP.");

                        model.addAttribute(
                                        "email",
                                        email);

                        return "customer/verify-otp";
                }

                // ---------------------------------------------------------
                // OTP SUCCESS
                // ---------------------------------------------------------

                session.setAttribute(
                                "passwordResetEmail",
                                email);

                session.setAttribute(
                                "otpVerified",
                                true);

                return "redirect:/reset-password?email=" + email;
        }

        // =========================================================
        // RESET PASSWORD PAGE
        // =========================================================

        @GetMapping("/reset-password")
        public String resetPasswordPage(
                        @RequestParam("email") String email,
                        HttpSession session,
                        Model model) {

                String verifiedEmail = (String) session.getAttribute(
                                "passwordResetEmail");

                Boolean otpVerified = (Boolean) session.getAttribute(
                                "otpVerified");

                // ---------------------------------------------------------
                // OTP verification required
                // ---------------------------------------------------------

                if (!Boolean.TRUE.equals(otpVerified)
                                || verifiedEmail == null
                                || !verifiedEmail.equalsIgnoreCase(email)) {

                        return "redirect:/forgot-password";
                }

                model.addAttribute(
                                "email",
                                email);

                return "customer/reset-password";
        }

        // =========================================================
        // RESET PASSWORD
        // =========================================================

        @PostMapping("/reset-password")
        public String resetPassword(
                        @RequestParam("email") String email,
                        @RequestParam("password") String password,
                        @RequestParam("confirmPassword") String confirmPassword,
                        HttpSession session,
                        Model model) {

                // ---------------------------------------------------------
                // Verify OTP session
                // ---------------------------------------------------------

                String verifiedEmail = (String) session.getAttribute(
                                "passwordResetEmail");

                Boolean otpVerified = (Boolean) session.getAttribute(
                                "otpVerified");

                if (!Boolean.TRUE.equals(otpVerified)
                                || verifiedEmail == null
                                || !verifiedEmail.equalsIgnoreCase(email)) {

                        return "redirect:/forgot-password";
                }

                // ---------------------------------------------------------
                // Password confirmation
                // ---------------------------------------------------------

                if (!password.equals(confirmPassword)) {

                        model.addAttribute(
                                        "error",
                                        "Passwords do not match.");

                        model.addAttribute(
                                        "email",
                                        email);

                        return "customer/reset-password";
                }

                // ---------------------------------------------------------
                // Basic password validation
                // ---------------------------------------------------------

                if (password.length() < 6) {

                        model.addAttribute(
                                        "error",
                                        "Password must contain at least 6 characters.");

                        model.addAttribute(
                                        "email",
                                        email);

                        return "customer/reset-password";
                }

                // ---------------------------------------------------------
                // Find customer
                // ---------------------------------------------------------

                Customer customer = customerService.getCustomerByEmail(email);

                if (customer == null) {

                        model.addAttribute(
                                        "error",
                                        "Customer not found.");

                        model.addAttribute(
                                        "email",
                                        email);

                        return "customer/reset-password";
                }

                // ---------------------------------------------------------
                // Encode new password with BCrypt
                // ---------------------------------------------------------

                customer.setPassword(
                                passwordEncoder.encode(password));

                customerService.updateCustomer(customer);

                // ---------------------------------------------------------
                // Delete used OTP
                // ---------------------------------------------------------

                passwordResetService.deleteByEmail(email);

                // ---------------------------------------------------------
                // Clear password-reset session data
                // ---------------------------------------------------------

                session.removeAttribute(
                                "passwordResetEmail");

                session.removeAttribute(
                                "otpVerified");

                // ---------------------------------------------------------
                // Login page
                // ---------------------------------------------------------

                return "redirect:/login?reset=success";
        }
        
}