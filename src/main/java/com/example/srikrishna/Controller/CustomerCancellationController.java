package com.example.srikrishna.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.srikrishna.Entity.Customer;
import com.example.srikrishna.Service.CancellationService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/Krishna/orders")
public class CustomerCancellationController {

    private final CancellationService cancellationService;

    public CustomerCancellationController(
            CancellationService cancellationService) {

        this.cancellationService =
                cancellationService;
    }

    // =====================================================
    // CANCELLATION PAGE
    // =====================================================

    @GetMapping("/cancel/{orderId}")
    public String cancellationPage(
            @PathVariable Long orderId,
            HttpSession session,
            Model model) {

        Customer customer =
                (Customer) session.getAttribute(
                        "loggedInCustomer"
                );

        if (customer == null) {
            return "redirect:/login";
        }

        model.addAttribute(
                "orderId",
                orderId
        );

        return "customer/cancellation";
    }

    // =====================================================
    // CANCEL ORDER
    // =====================================================

    @PostMapping("/cancel/{orderId}")
    public String cancelOrder(
            @PathVariable Long orderId,

            @RequestParam("reason")
            String reason,

            HttpSession session,

            RedirectAttributes redirectAttributes) {

        Customer customer =
                (Customer) session.getAttribute(
                        "loggedInCustomer"
                );

        if (customer == null) {
            return "redirect:/login";
        }

        try {

            cancellationService.cancelOrder(
                    orderId,
                    customer.getId(),
                    reason
            );

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Order cancelled successfully. " +
                    "Refund will be processed according " +
                    "to the refund policy."
            );

        } catch (RuntimeException e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage()
            );
        }

        return "redirect:/Krishna/orders";
    }
}