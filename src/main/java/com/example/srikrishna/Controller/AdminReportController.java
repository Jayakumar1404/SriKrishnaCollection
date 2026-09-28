package com.example.srikrishna.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.srikrishna.Service.AdminReportService;

@Controller
@RequestMapping("/admin")
public class AdminReportController {

    private final AdminReportService adminReportService;

    public AdminReportController(
            AdminReportService adminReportService) {

        this.adminReportService =
                adminReportService;
    }

    @GetMapping("/reports")
    public String reports(Model model) {

        model.addAttribute(
                "payments",
                adminReportService.getAllPayments()
        );

        model.addAttribute(
                "totalPayments",
                adminReportService.getTotalPayments()
        );

        model.addAttribute(
                "successfulPayments",
                adminReportService.getSuccessfulPayments()
        );

        model.addAttribute(
                "pendingPayments",
                adminReportService.getPendingPayments()
        );

        model.addAttribute(
                "failedPayments",
                adminReportService.getFailedPayments()
        );

        model.addAttribute(
                "codPayments",
                adminReportService.getCodPayments()
        );

        model.addAttribute(
                "razorpayPayments",
                adminReportService.getRazorpayPayments()
        );

        model.addAttribute(
                "successfulRevenue",
                adminReportService.getSuccessfulRevenue()
        );

        return "admin/reports";
    }
}