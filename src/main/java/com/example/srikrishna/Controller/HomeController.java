package com.example.srikrishna.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.srikrishna.Entity.Category;
import com.example.srikrishna.Entity.Product;
import com.example.srikrishna.Service.CategoryService;
import com.example.srikrishna.Service.ProductService;

@Controller
@RequestMapping("/Krishna")
public class HomeController {

        @Autowired
        private ProductService productService;

        @Autowired
        private CategoryService categoryService;

        @GetMapping("/")
        public String home(Model model) {

                List<Product> products = productService.getActiveProducts();

                List<Category> categories = categoryService.filterCategories("ACTIVE");

                model.addAttribute("products", products);
                model.addAttribute("categories", categories);

                return "customer/home";
        }
}