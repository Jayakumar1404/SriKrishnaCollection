package com.example.srikrishna.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.srikrishna.Repository.OrderRepository;
import com.example.srikrishna.Service.CustomerService;
import com.example.srikrishna.Service.ProductService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class AdminAuthController {

        /*
         * BCrypt hash of:
         *
         * Admin@123
         */
        private static final String ADMIN_PASSWORD = "$2a$10$p5ZEhQQiyWDraTY.ZyKQROhgJxelbjWZtuks4XN7Cig/HFSQQqP/6";

        private static final String ADMIN_USERNAME = "admin";

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Autowired
        private CustomerService customerService;

        @Autowired
        private ProductService productService;

        @Autowired
        private final OrderRepository orderRepository;

        public AdminAuthController(
                        CustomerService customerService,
                        ProductService productService,
                        OrderRepository orderRepository) {
                this.customerService = customerService;
                this.productService = productService;
                this.orderRepository = orderRepository;
        }

        // =========================================================
        // ADMIN LOGIN PAGE
        // =========================================================

        @GetMapping("/admin/login")
        public String adminLoginPage() {

                return "admin/login";
        }

        // =========================================================
        // ADMIN LOGIN
        // =========================================================

        @PostMapping("/admin/login")
        public String adminLogin(
                        @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        HttpServletRequest request,
                        Model model) {

                // ---------------------------------------------------------
                // CHECK USERNAME
                // ---------------------------------------------------------

                if (!ADMIN_USERNAME.equals(username)) {

                        model.addAttribute(
                                        "error",
                                        "Invalid username or password");

                        return "admin/login";
                }

                // ---------------------------------------------------------
                // CHECK PASSWORD USING BCrypt
                // ---------------------------------------------------------

                if (!passwordEncoder.matches(
                                password,
                                ADMIN_PASSWORD)) {

                        model.addAttribute(
                                        "error",
                                        "Invalid username or password");

                        return "admin/login";
                }

                // ---------------------------------------------------------
                // SESSION FIXATION PROTECTION
                // ---------------------------------------------------------

                HttpSession oldSession = request.getSession(false);

                if (oldSession != null) {
                        oldSession.invalidate();
                }

                HttpSession session = request.getSession(true);

                // ---------------------------------------------------------
                // CREATE ADMIN SESSION
                // ---------------------------------------------------------

                session.setAttribute(
                                "loggedInAdmin",
                                username);

                session.setAttribute(
                                "adminAuthenticated",
                                true);

                session.setAttribute(
                                "role",
                                "ADMIN");

                // ---------------------------------------------------------
                // REDIRECT TO DASHBOARD
                // ---------------------------------------------------------

                return "redirect:/admin/dashboard";
        }

        // =========================================================
        // ADMIN DASHBOARD
        // =========================================================
        @GetMapping("/admin/dashboard")
        public String adminDashboard(
                        HttpSession session,
                        Model model) {

                // ---------------------------------------------------------
                // CHECK ADMIN SESSION
                // ---------------------------------------------------------

                Object admin = session.getAttribute("loggedInAdmin");

                if (admin == null) {
                        return "redirect:/admin/login";
                }

                // ---------------------------------------------------------
                // CUSTOMER COUNT
                // ---------------------------------------------------------

                long customerCount = customerService
                                .getAllCustomers()
                                .size();

                // ---------------------------------------------------------
                // PRODUCT DATA
                // ---------------------------------------------------------

                var products = productService.getAllProducts();

                // ---------------------------------------------------------
                // PRODUCT COUNT
                // ---------------------------------------------------------

                long productCount = products.size();

                // ---------------------------------------------------------
                // LOW STOCK COUNT
                // ---------------------------------------------------------

                long lowStockCount = products.stream()
                                .filter(product -> product.getStock() != null
                                                && product.getStock() < 2)
                                .count();

                // ---------------------------------------------------------
                // ORDER COUNT
                // ---------------------------------------------------------

                long orderCount = orderRepository.count();

                // ---------------------------------------------------------
                // TOTAL REVENUE
                // ---------------------------------------------------------

                double totalRevenue = orderRepository.findAll()
                                .stream()
                                .filter(order -> order.getTotalAmount() != null)
                                .mapToDouble(order -> order.getTotalAmount())
                                .sum();

                // ---------------------------------------------------------
                // SEND DATA TO THYMELEAF
                // ---------------------------------------------------------

                model.addAttribute(
                                "customerCount",
                                customerCount);

                model.addAttribute(
                                "productCount",
                                productCount);

                model.addAttribute(
                                "orderCount",
                                orderCount);

                model.addAttribute(
                                "totalRevenue",
                                totalRevenue);

                model.addAttribute(
                                "lowStockCount",
                                lowStockCount);

                model.addAttribute(
                                "products",
                                products);

                model.addAttribute(
                                "adminUsername",
                                admin);

                return "admin/dashboard";
        }

        // =========================================================
        // ADMIN LOGOUT
        // =========================================================

        @GetMapping("/admin/logout")
        public String adminLogout(
                        HttpSession session) {

                if (session != null) {

                        session.invalidate();
                }

                return "redirect:/admin/login?logout=success";
        }
}
