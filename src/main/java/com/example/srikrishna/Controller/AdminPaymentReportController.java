package com.example.srikrishna.Controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.srikrishna.Entity.Payment;
import com.example.srikrishna.Service.PaymentService;

@Controller
@RequestMapping("/admin")
public class AdminPaymentReportController {

    private final PaymentService paymentService;

    public AdminPaymentReportController(
            PaymentService paymentService) {

        this.paymentService = paymentService;
    }

    // =====================================================
    // PAYMENT REPORT
    // =====================================================

    @GetMapping("/payment-report")
    public String paymentReport(Model model) {

        List<Payment> payments =
                paymentService.getAllPayments();

        model.addAttribute(
                "payments",
                payments
        );

        // =================================================
        // PAYMENT COUNTS
        // =================================================

        model.addAttribute(
                "totalPayments",
                paymentService.countAll()
        );

        model.addAttribute(
                "successfulPayments",
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

        return "admin/payment-report";
    }
}