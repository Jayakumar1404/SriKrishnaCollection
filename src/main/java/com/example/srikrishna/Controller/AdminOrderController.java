package com.example.srikrishna.Controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.srikrishna.Entity.Order;
import com.example.srikrishna.Service.OrderService;

@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {

    private final OrderService orderService;


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public AdminOrderController(
            OrderService orderService) {

        this.orderService = orderService;
    }


    // =====================================================
    // ADMIN ORDERS
    // SEARCH + FILTER + DASHBOARD STATISTICS
    // =====================================================

    @GetMapping
    public String orders(

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

        List<Order> orders =
                orderService.searchAndFilterOrders(
                        keyword,
                        status
                );


        model.addAttribute(
                "orders",
                orders
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
        // TODAY START
        // =================================================

        LocalDate today =
                LocalDate.now();


        LocalDateTime startOfDay =
                today.atStartOfDay();


        // =================================================
        // TOMORROW START
        // =================================================

        LocalDateTime startOfTomorrow =
                today
                        .plusDays(1)
                        .atStartOfDay();


        // =================================================
        // TOTAL ORDERS
        // =================================================

        model.addAttribute(
                "totalOrders",
                orderService.getTotalOrders()
        );


        // =================================================
        // TODAY'S ORDERS
        // =================================================

        model.addAttribute(
                "todayOrders",
                orderService.getTodayOrders(
                        startOfDay,
                        startOfTomorrow
                )
        );


        // =================================================
        // PENDING ORDERS
        // =================================================

        model.addAttribute(
                "pendingOrders",
                orderService.getPendingOrders()
        );


        // =================================================
        // SHIPPED ORDERS
        // =================================================

        model.addAttribute(
                "shippedOrders",
                orderService.getShippedOrders()
        );


        // =================================================
        // DELIVERED ORDERS
        // =================================================

        model.addAttribute(
                "deliveredOrders",
                orderService.getDeliveredOrders()
        );


        // =================================================
        // CANCELLED ORDERS
        // =================================================

        model.addAttribute(
                "cancelledOrders",
                orderService.getCancelledOrders()
        );


        // =================================================
        // TOTAL REVENUE
        // =================================================

        model.addAttribute(
                "totalRevenue",
                orderService.getTotalRevenue()
        );


        // =================================================
        // TODAY'S REVENUE
        // =================================================

        model.addAttribute(
                "todayRevenue",
                orderService.getTodayRevenue(
                        startOfDay,
                        startOfTomorrow
                )
        );


        // =================================================
        // RETURN PAGE
        // =================================================

        return "admin/orders";
    }


    // =====================================================
    // ADMIN ORDER DETAILS
    // =====================================================

    @GetMapping("/{orderId}")
    public String orderDetails(

            @PathVariable Long orderId,

            Model model,

            RedirectAttributes redirectAttributes) {


        Order order =
                orderService.getOrderById(
                        orderId
                );


        // -------------------------------------------------
        // ORDER NOT FOUND
        // -------------------------------------------------

        if (order == null) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Order not found."
            );

            return "redirect:/admin/orders";
        }


        // -------------------------------------------------
        // SEND ORDER
        // -------------------------------------------------

        model.addAttribute(
                "order",
                order
        );


        return "admin/order-details";
    }


    // =====================================================
    // ADMIN UPDATE ORDER STATUS
    // =====================================================

    @PostMapping("/{orderId}/status")
    public String updateOrderStatus(

            @PathVariable Long orderId,

            @RequestParam("status")
            String status,

            RedirectAttributes redirectAttributes) {


        try {

            orderService.updateOrderStatus(
                    orderId,
                    status
            );


            redirectAttributes.addFlashAttribute(
                    "success",
                    "Order status updated successfully."
            );


        } catch (RuntimeException e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage()
            );
        }


        return "redirect:/admin/orders/" + orderId;
    }

}