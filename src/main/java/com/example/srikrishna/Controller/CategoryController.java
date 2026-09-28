package com.example.srikrishna.Controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.srikrishna.Entity.Category;
import com.example.srikrishna.Entity.Customer;
import com.example.srikrishna.Service.CategoryService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/Krishna")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // =========================================================
    // CUSTOMER CATEGORIES
    // =========================================================

    @GetMapping("/categories")
    public String categories(
            HttpSession session,
            Model model) {

        // ---------------------------------------------------------
        // GET LOGGED-IN CUSTOMER
        // ---------------------------------------------------------

        Customer customer =
                (Customer) session.getAttribute(
                        "loggedInCustomer");

        // ---------------------------------------------------------
        // CUSTOMER NOT LOGGED IN
        // ---------------------------------------------------------

        if (customer == null) {

            return "redirect:/login";
        }

        // ---------------------------------------------------------
        // CUSTOMER STATUS CHECK
        // ---------------------------------------------------------

        if (customer.getStatus() == null
                || !customer.getStatus()) {

            session.invalidate();

            return "redirect:/login?error=inactive";
        }

        // ---------------------------------------------------------
        // GET CATEGORIES
        // ---------------------------------------------------------

        List<Category> categories =
                categoryService.getAllCategories();

        // ---------------------------------------------------------
        // SEND DATA TO VIEW
        // ---------------------------------------------------------

        model.addAttribute(
                "categories",
                categories);

        model.addAttribute(
                "customer",
                customer);

        model.addAttribute(
                "customerId",
                customer.getId());

        // ---------------------------------------------------------
        // CATEGORY PAGE
        // ---------------------------------------------------------

        return "customer/categories";
    }
}