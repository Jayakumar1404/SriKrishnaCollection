package com.example.srikrishna.Controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.srikrishna.Entity.Payment;
import com.example.srikrishna.Service.PaymentService;

@Controller
@RequestMapping("/admin")
public class AdminPaymentController {

    private final PaymentService paymentService;

    public AdminPaymentController(
            PaymentService paymentService) {

        this.paymentService = paymentService;
    }

    // =====================================================
    // PAYMENT MANAGEMENT
    // =====================================================

    @GetMapping("/payments")
    public String payments(

            @RequestParam(
                    value = "filter",
                    required = false,
                    defaultValue = "ALL"
            )
            String filter,

            Model model) {

        List<Payment> payments;

        switch (filter.toUpperCase()) {

            case "SUCCESS":

                payments = paymentService
                        .getPaymentsByStatus("SUCCESS");

                break;

            case "PENDING":

                payments = paymentService
                        .getPaymentsByStatus("PENDING");

                break;

            case "FAILED":

                payments = paymentService
                        .getPaymentsByStatus("FAILED");

                break;

            case "COD":

                payments = paymentService
                        .getPaymentsByMethod("COD");

                break;

            case "RAZORPAY":

                payments = paymentService
                        .getPaymentsByMethod("RAZORPAY");

                break;

            default:

                payments = paymentService
                        .getAllPayments();

                filter = "ALL";

                break;
        }

        // Payment list
        model.addAttribute(
                "payments",
                payments
        );

        // Active filter
        model.addAttribute(
                "activeFilter",
                filter
        );

        // Statistics
        model.addAttribute(
                "totalPayments",
                paymentService.countAll()
        );

        model.addAttribute(
                "successPayments",
                paymentService.countSuccess()
        );

        model.addAttribute(
                "pendingPayments",
                paymentService.countPending()
        );

        model.addAttribute(
                "failedPayments",
                paymentService.countFailed()
        );

        model.addAttribute(
                "codPayments",
                paymentService.countCod()
        );

        model.addAttribute(
                "razorpayPayments",
                paymentService.countRazorpay()
        );

        return "admin/payments";
    }

    // =====================================================
    // PAYMENT DETAILS
    // =====================================================

    @GetMapping("/payments/{id}")
    public String paymentDetails(
            @PathVariable Long id,
            Model model) {

        Payment payment =
                paymentService.getPaymentById(id);

        if (payment == null) {

            return "redirect:/admin/payments";
        }

        model.addAttribute(
                "payment",
                payment
        );

        return "admin/payment-details";
    }
}