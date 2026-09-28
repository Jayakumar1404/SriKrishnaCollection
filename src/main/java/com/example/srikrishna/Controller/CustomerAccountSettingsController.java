package com.example.srikrishna.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.srikrishna.Entity.Customer;
import com.example.srikrishna.Service.CustomerService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/Krishna/account-settings")
public class CustomerAccountSettingsController {

    private final CustomerService customerService;

    public CustomerAccountSettingsController(
            CustomerService customerService) {

        this.customerService = customerService;
    }


    // =====================================================
    // GET ACCOUNT SETTINGS
    // GET /Krishna/account-settings
    // =====================================================

    @GetMapping
    public String accountSettings(
            HttpSession session,
            Model model) {

        Customer sessionCustomer =
                (Customer) session.getAttribute(
                        "loggedInCustomer"
                );

        // Customer must login
        if (sessionCustomer == null) {

            return "redirect:/login";
        }


        // Get latest customer data from database
        Customer customer =
                customerService.getCustomerById(
                        sessionCustomer.getId()
                );


        if (customer == null) {

            session.invalidate();

            return "redirect:/login";
        }


        // Check account status
        if (customer.getStatus() == null ||
                !customer.getStatus()) {

            session.invalidate();

            return "redirect:/login?error=inactive";
        }


        // Update session with latest information
        session.setAttribute(
                "loggedInCustomer",
                customer
        );


        model.addAttribute(
                "customer",
                customer
        );


        return "customer/account-settings";
    }


    // =====================================================
    // UPDATE PROFILE
    // POST /Krishna/account-settings/profile
    // =====================================================

    @PostMapping("/profile")
    public String updateProfile(

            @RequestParam String firstName,

            @RequestParam String lastName,

            @RequestParam String mobile,

            @RequestParam(required = false)
            String gender,

            @RequestParam(required = false)
            String address,

            @RequestParam(required = false)
            String city,

            @RequestParam(required = false)
            String state,

            @RequestParam(required = false)
            String pincode,

            HttpSession session,

            RedirectAttributes redirectAttributes) {

        Customer sessionCustomer =
                (Customer) session.getAttribute(
                        "loggedInCustomer"
                );


        if (sessionCustomer == null) {

            return "redirect:/login";
        }


        try {

            Customer updatedCustomer =
                    customerService.updateProfile(

                            sessionCustomer.getId(),

                            firstName,

                            lastName,

                            mobile,

                            gender,

                            address,

                            city,

                            state,

                            pincode
                    );


            // Update session
            session.setAttribute(
                    "loggedInCustomer",
                    updatedCustomer
            );


            redirectAttributes.addFlashAttribute(
                    "success",
                    "Profile updated successfully."
            );


        } catch (Exception e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage()
            );
        }


        return "redirect:/Krishna/account-settings";
    }


    // =====================================================
    // UPLOAD PROFILE IMAGE
    // POST /Krishna/account-settings/image
    // =====================================================

    @PostMapping("/image")
    public String uploadProfileImage(

            @RequestParam("image")
            MultipartFile image,

            HttpSession session,

            RedirectAttributes redirectAttributes) {

        Customer sessionCustomer =
                (Customer) session.getAttribute(
                        "loggedInCustomer"
                );


        if (sessionCustomer == null) {

            return "redirect:/login";
        }


        try {

            Customer updatedCustomer =
                    customerService.updateProfileImage(

                            sessionCustomer.getId(),

                            image
                    );


            // Update session
            session.setAttribute(
                    "loggedInCustomer",
                    updatedCustomer
            );


            redirectAttributes.addFlashAttribute(
                    "success",
                    "Profile image updated successfully."
            );


        } catch (Exception e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage()
            );
        }


        return "redirect:/Krishna/account-settings";
    }


    // =====================================================
    // REMOVE PROFILE IMAGE
    // POST /Krishna/account-settings/remove-image
    // =====================================================

    @PostMapping("/remove-image")
    public String removeProfileImage(

            HttpSession session,

            RedirectAttributes redirectAttributes) {

        Customer sessionCustomer =
                (Customer) session.getAttribute(
                        "loggedInCustomer"
                );


        if (sessionCustomer == null) {

            return "redirect:/login";
        }


        try {

            Customer updatedCustomer =
                    customerService.removeProfileImage(

                            sessionCustomer.getId()
                    );


            // Update session
            session.setAttribute(
                    "loggedInCustomer",
                    updatedCustomer
            );


            redirectAttributes.addFlashAttribute(
                    "success",
                    "Profile image removed successfully."
            );


        } catch (Exception e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage()
            );
        }


        return "redirect:/Krishna/account-settings";
    }


    // =====================================================
    // CHANGE PASSWORD
    // POST /Krishna/account-settings/password
    // =====================================================

    @PostMapping("/password")
    public String changePassword(

            @RequestParam String currentPassword,

            @RequestParam String newPassword,

            @RequestParam String confirmPassword,

            HttpSession session,

            RedirectAttributes redirectAttributes) {

        Customer sessionCustomer =
                (Customer) session.getAttribute(
                        "loggedInCustomer"
                );


        if (sessionCustomer == null) {

            return "redirect:/login";
        }


        try {

            customerService.changePassword(

                    sessionCustomer.getId(),

                    currentPassword,

                    newPassword,

                    confirmPassword
            );


            redirectAttributes.addFlashAttribute(
                    "success",
                    "Password changed successfully. Please login again."
            );


            /*
             * Logout after password change.
             * This forces the customer to login
             * using the new password.
             */

            session.invalidate();


            return "redirect:/login?passwordChanged=true";


        } catch (Exception e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage()
            );


            return "redirect:/Krishna/account-settings";
        }
    }


    // =====================================================
    // LOGOUT
    // POST /Krishna/account-settings/logout
    // =====================================================

    @PostMapping("/logout")
    public String logout(
            HttpSession session) {

        session.invalidate();

        return "redirect:/login?logout=true";
    }
}