package com.example.srikrishna.Controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.srikrishna.Entity.Customer;
import com.example.srikrishna.Service.CustomerService;

@Controller
@RequestMapping("/admin/customers")
public class AdminCustomerController {


    private final CustomerService customerService;


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public AdminCustomerController(
            CustomerService customerService) {

        this.customerService =
                customerService;
    }


    // =====================================================
    // CUSTOMER LIST
    // SEARCH + FILTER + STATISTICS
    // =====================================================

    @GetMapping
    public String customers(

            @RequestParam(
                    value = "keyword",
                    required = false
            )
            String keyword,

            @RequestParam(
                    value = "status",
                    required = false,
                    defaultValue = "ALL"
            )
            String status,

            Model model) {


        // =================================================
        // SEARCH + FILTER
        // =================================================

        List<Customer> customers =
                customerService
                        .searchAndFilterCustomers(
                                keyword,
                                status
                        );


        model.addAttribute(
                "customers",
                customers
        );


        model.addAttribute(
                "keyword",
                keyword
        );


        model.addAttribute(
                "selectedStatus",
                status
        );


        // =================================================
        // TODAY
        // =================================================

        LocalDate today =
                LocalDate.now();


        LocalDateTime startOfDay =
                today.atStartOfDay();


        LocalDateTime startOfTomorrow =
                today
                        .plusDays(1)
                        .atStartOfDay();


        // =================================================
        // STATISTICS
        // =================================================

        model.addAttribute(
                "totalCustomers",
                customerService
                        .getTotalCustomers()
        );


        model.addAttribute(
                "activeCustomers",
                customerService
                        .getActiveCustomers()
        );


        model.addAttribute(
                "inactiveCustomers",
                customerService
                        .getInactiveCustomers()
        );


        model.addAttribute(
                "todayCustomers",
                customerService
                        .getTodayCustomers(
                                startOfDay,
                                startOfTomorrow
                        )
        );


        return "admin/customers";
    }


    // =====================================================
    // CUSTOMER DETAILS
    // =====================================================

    @GetMapping("/{id}")
    public String customerDetails(

            @PathVariable Long id,

            Model model,

            RedirectAttributes redirectAttributes) {


        Customer customer =
                customerService
                        .getCustomerById(id);


        if (customer == null) {

            redirectAttributes
                    .addFlashAttribute(
                            "error",
                            "Customer not found."
                    );

            return "redirect:/admin/customers";
        }


        model.addAttribute(
                "customer",
                customer
        );


        return "admin/customer-details";
    }


    // =====================================================
    // ACTIVATE
    // =====================================================

    @PostMapping("/{id}/activate")
    public String activateCustomer(

            @PathVariable Long id,

            RedirectAttributes redirectAttributes) {

        try {

            customerService
                    .activateCustomer(id);


            redirectAttributes
                    .addFlashAttribute(
                            "success",
                            "Customer activated successfully."
                    );

        } catch (RuntimeException e) {

            redirectAttributes
                    .addFlashAttribute(
                            "error",
                            e.getMessage()
                    );
        }


        return "redirect:/admin/customers";
    }


    // =====================================================
    // DEACTIVATE
    // =====================================================

    @PostMapping("/{id}/deactivate")
    public String deactivateCustomer(

            @PathVariable Long id,

            RedirectAttributes redirectAttributes) {

        try {

            customerService
                    .deactivateCustomer(id);


            redirectAttributes
                    .addFlashAttribute(
                            "success",
                            "Customer deactivated successfully."
                    );

        } catch (RuntimeException e) {

            redirectAttributes
                    .addFlashAttribute(
                            "error",
                            e.getMessage()
                    );
        }


        return "redirect:/admin/customers";
    }


    // =====================================================
    // DELETE
    // =====================================================

    @PostMapping("/{id}/delete")
    public String deleteCustomer(

            @PathVariable Long id,

            RedirectAttributes redirectAttributes) {

        try {

            customerService
                    .deleteCustomer(id);


            redirectAttributes
                    .addFlashAttribute(
                            "success",
                            "Customer deleted successfully."
                    );

        } catch (RuntimeException e) {

            redirectAttributes
                    .addFlashAttribute(
                            "error",
                            e.getMessage()
                    );
        }


        return "redirect:/admin/customers";
    }

}