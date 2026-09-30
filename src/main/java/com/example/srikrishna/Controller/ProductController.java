package com.example.srikrishna.Controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.srikrishna.Entity.Category;
import com.example.srikrishna.Entity.Customer;
import com.example.srikrishna.Entity.Product;
import com.example.srikrishna.Entity.ProductImage;
import com.example.srikrishna.Repository.ProductImageRepository;
import com.example.srikrishna.Service.CategoryService;
import com.example.srikrishna.Service.ProductService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/Krishna")
public class ProductController {

    private final ProductService productService;
    private final ProductImageRepository productImageRepository;
    private final CategoryService categoryService;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public ProductController(
            ProductService productService,
            ProductImageRepository productImageRepository,
            CategoryService categoryService) {

        this.productService = productService;
        this.productImageRepository = productImageRepository;
        this.categoryService = categoryService;
    }

    // =====================================================
    // PRODUCT LIST
    // =====================================================

    @GetMapping("/product")
    public String products(
            @RequestParam(required = false) String category,
            Model model) {

        List<Product> products;

        /*
         * =================================================
         * ALL PRODUCTS
         * =================================================
         *
         * URL:
         * /Krishna/product
         *
         * Shows all ACTIVE products.
         */

        if (category == null || category.trim().isEmpty()) {

            products = productService.getActiveProducts();

        }

        /*
         * =================================================
         * CATEGORY PRODUCTS
         * =================================================
         *
         * URL example:
         *
         * /Krishna/product?category=Preminum%20Top
         *
         * Shows only ACTIVE products belonging
         * to the selected category.
         */

        else {

            products = productService.getProductsByCategory(
                    category.trim()
            );
        }

        /*
         * =================================================
         * ACTIVE CATEGORIES
         * =================================================
         *
         * These categories come from Admin.
         *
         * No hard-coded categories such as:
         *
         
         *
         * are required.
         */

        List<Category> categories =
                categoryService.filterCategories("ACTIVE");

        /*
         * =================================================
         * SEND DATA TO THYMELEAF
         * =================================================
         */

        model.addAttribute(
                "products",
                products
        );

        model.addAttribute(
                "categories",
                categories
        );

        model.addAttribute(
                "selectedCategory",
                category
        );

        return "customer/products";
    }

    // =====================================================
    // PRODUCT DETAILS
    // =====================================================

    @GetMapping("/product/{id}")
    public String productDetails(

            @PathVariable Long id,

            Model model,

            HttpSession session) {

        Product product =
                productService.getProductById(id);

        if (product == null) {

            return "redirect:/Krishna/product";
        }

        model.addAttribute(
                "product",
                product
        );

        List<ProductImage> productImages =
                productImageRepository
                        .findByProductIdOrderByPrimaryImageDescIdAsc(id);

        model.addAttribute(
                "productImages",
                productImages
        );

        Customer customer =
                (Customer) session.getAttribute(
                        "loggedInCustomer"
                );

        if (customer != null) {

            model.addAttribute(
                    "customerId",
                    customer.getId()
            );
        }

        return "customer/product-details";
    }

    // =====================================================
    // BUY NOW
    // =====================================================

    @GetMapping("/product/buy-now/{id}")
    public String buyNow(

            @PathVariable Long id,

            HttpSession session,

            RedirectAttributes redirectAttributes) {

        Customer customer =
                (Customer) session.getAttribute(
                        "loggedInCustomer"
                );

        // -------------------------------------------------
        // CUSTOMER LOGIN CHECK
        // -------------------------------------------------

        if (customer == null) {

            return "redirect:/login";
        }

        // -------------------------------------------------
        // CUSTOMER STATUS CHECK
        // -------------------------------------------------

        if (customer.getStatus() == null
                || !customer.getStatus()) {

            session.invalidate();

            return "redirect:/login?error=inactive";
        }

        // -------------------------------------------------
        // GET PRODUCT
        // -------------------------------------------------

        Product product =
                productService.getProductById(id);

        if (product == null) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Product not found."
            );

            return "redirect:/Krishna/product";
        }

        // -------------------------------------------------
        // PRODUCT STATUS CHECK
        // -------------------------------------------------

        if (product.getStatus() == null
                || !product.getStatus()) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "This product is currently unavailable."
            );

            return "redirect:/Krishna/product";
        }

        // -------------------------------------------------
        // STOCK CHECK
        // -------------------------------------------------

        if (product.getStock() == null
                || product.getStock() <= 0) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "This product is out of stock."
            );

            return "redirect:/Krishna/product";
        }

        // =================================================
        // BUY NOW SESSION
        // =================================================

        session.setAttribute(
                "buyNowProductId",
                product.getId()
        );

        session.setAttribute(
                "buyNowQuantity",
                1
        );

        session.setAttribute(
                "buyNowMode",
                true
        );

        return "redirect:/Krishna/checkout?buyNow=true";
    }
}