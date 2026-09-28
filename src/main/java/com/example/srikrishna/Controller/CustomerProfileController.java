package com.example.srikrishna.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.srikrishna.Entity.Customer;
import com.example.srikrishna.Service.CustomerService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/Krishna/profile")
public class CustomerProfileController {

    private final CustomerService customerService;

    public CustomerProfileController(
            CustomerService customerService) {

        this.customerService = customerService;
    }

    // =====================================================
    // VIEW PROFILE
    // =====================================================

    @GetMapping
    public String profile(
            HttpSession session,
            Model model) {

        Customer loggedInCustomer =
                (Customer) session.getAttribute(
                        "loggedInCustomer"
                );

        // -------------------------------------------------
        // LOGIN CHECK
        // -------------------------------------------------

        if (loggedInCustomer == null) {

            return "redirect:/login";
        }

        // -------------------------------------------------
        // STATUS CHECK
        // -------------------------------------------------

        if (loggedInCustomer.getStatus() == null
                || !loggedInCustomer.getStatus()) {

            session.invalidate();

            return "redirect:/login?error=inactive";
        }

        // -------------------------------------------------
        // GET LATEST CUSTOMER DATA
        // -------------------------------------------------

        Customer customer =
                customerService.getCustomerById(
                        loggedInCustomer.getId()
                );

        if (customer == null) {

            session.invalidate();

            return "redirect:/login";
        }

        model.addAttribute(
                "customer",
                customer
        );

        return "customer/profile";
    }

    // =====================================================
    // EDIT PROFILE
    // =====================================================

    @PostMapping("/update")
    public String updateProfile(

            @RequestParam("firstName")
            String firstName,

            @RequestParam("lastName")
            String lastName,

            @RequestParam("mobile")
            String mobile,

            @RequestParam("gender")
            String gender,

            @RequestParam("address")
            String address,

            @RequestParam("city")
            String city,

            @RequestParam("state")
            String state,

            @RequestParam("pincode")
            String pincode,

            HttpSession session,

            RedirectAttributes redirectAttributes) {

        // =================================================
        // LOGIN CHECK
        // =================================================

        Customer loggedInCustomer =
                (Customer) session.getAttribute(
                        "loggedInCustomer"
                );

        if (loggedInCustomer == null) {

            return "redirect:/login";
        }

        // =================================================
        // STATUS CHECK
        // =================================================

        if (loggedInCustomer.getStatus() == null
                || !loggedInCustomer.getStatus()) {

            session.invalidate();

            return "redirect:/login?error=inactive";
        }

        try {

            // =============================================
            // GET CUSTOMER FROM DATABASE
            // =============================================

            Customer customer =
                    customerService.getCustomerById(
                            loggedInCustomer.getId()
                    );

            if (customer == null) {

                throw new RuntimeException(
                        "Customer account not found."
                );
            }

            // =============================================
            // VALIDATION
            // =============================================

            if (firstName == null
                    || firstName.trim().isEmpty()) {

                throw new RuntimeException(
                        "First name is required."
                );
            }

            if (lastName == null
                    || lastName.trim().isEmpty()) {

                throw new RuntimeException(
                        "Last name is required."
                );
            }

            if (mobile == null
                    || mobile.trim().isEmpty()) {

                throw new RuntimeException(
                        "Mobile number is required."
                );
            }

            if (!mobile.matches("\\d{10}")) {

                throw new RuntimeException(
                        "Mobile number must contain exactly 10 digits."
                );
            }

            if (pincode == null
                    || !pincode.matches("\\d{6}")) {

                throw new RuntimeException(
                        "Pincode must contain exactly 6 digits."
                );
            }

            // =============================================
            // UPDATE PERSONAL DETAILS
            // =============================================

            customer.setFirstName(
                    firstName.trim()
            );

            customer.setLastName(
                    lastName.trim()
            );

            customer.setMobile(
                    mobile.trim()
            );

            customer.setGender(
                    gender != null
                            ? gender.trim()
                            : ""
            );

            // =============================================
            // UPDATE ADDRESS
            // =============================================

            customer.setAddress(
                    address != null
                            ? address.trim()
                            : ""
            );

            customer.setCity(
                    city != null
                            ? city.trim()
                            : ""
            );

            customer.setState(
                    state != null
                            ? state.trim()
                            : ""
            );

            customer.setPincode(
                    pincode.trim()
            );

            // =============================================
            // SAVE
            // =============================================

            Customer updatedCustomer =
                    customerService.saveCustomer(
                            customer
                    );

            // =============================================
            // UPDATE SESSION
            // =============================================

            session.setAttribute(
                    "loggedInCustomer",
                    updatedCustomer
            );

            // =============================================
            // SUCCESS MESSAGE
            // =============================================

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Profile updated successfully."
            );

        } catch (RuntimeException e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage()
            );
        }

        return "redirect:/Krishna/profile";
    }
}