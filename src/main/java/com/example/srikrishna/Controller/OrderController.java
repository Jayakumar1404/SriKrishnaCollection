package com.example.srikrishna.Controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.srikrishna.Entity.Customer;
import com.example.srikrishna.Entity.Order;
import com.example.srikrishna.Service.OrderService;

import jakarta.servlet.http.HttpSession;

@Controller
public class OrderController {

    private final OrderService orderService;


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public OrderController(OrderService orderService) {

        this.orderService = orderService;
    }


    // =====================================================
    // CUSTOMER ORDERS
    // =====================================================

    @GetMapping("/Krishna/orders")
    public String customerOrders(
            HttpSession session,
            Model model) {


        // -------------------------------------------------
        // GET LOGGED-IN CUSTOMER
        // -------------------------------------------------

        Customer customer =
                (Customer) session.getAttribute(
                        "loggedInCustomer"
                );


        // -------------------------------------------------
        // LOGIN CHECK
        // -------------------------------------------------

        if (customer == null) {

            return "redirect:/login";
        }


        // -------------------------------------------------
        // GET CUSTOMER ID
        // -------------------------------------------------

        Long customerId =
                customer.getId();


        // -------------------------------------------------
        // GET ONLY THIS CUSTOMER'S ORDERS
        // -------------------------------------------------

        List<Order> orders =
                orderService
                        .getOrdersByCustomerId(customerId);


        // -------------------------------------------------
        // SEND TO HTML
        // -------------------------------------------------

        model.addAttribute(
                "orders",
                orders
        );


        // -------------------------------------------------
        // RETURN PAGE
        // -------------------------------------------------

        return "customer/orders";
    }


    // =====================================================
    // CUSTOMER ORDER DETAILS
    // =====================================================

    @GetMapping("/Krishna/orders/{orderId}")
    public String customerOrderDetails(
            @PathVariable Long orderId,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {


        // -------------------------------------------------
        // GET LOGGED-IN CUSTOMER
        // -------------------------------------------------

        Customer customer =
                (Customer) session.getAttribute(
                        "loggedInCustomer"
                );


        // -------------------------------------------------
        // LOGIN CHECK
        // -------------------------------------------------

        if (customer == null) {

            return "redirect:/login";
        }


        Long customerId =
                customer.getId();


        // -------------------------------------------------
        // FIND ORDER
        // -------------------------------------------------

        Order order =
                orderService.getOrderById(orderId);


        // -------------------------------------------------
        // ORDER NOT FOUND
        // -------------------------------------------------

        if (order == null) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Order not found."
            );

            return "redirect:/Krishna/orders";
        }


        // -------------------------------------------------
        // SECURITY CHECK
        // -------------------------------------------------

        if (order.getCustomer() == null
                || order.getCustomer().getId() == null
                || !order.getCustomer()
                        .getId()
                        .equals(customerId)) {


            redirectAttributes.addFlashAttribute(
                    "error",
                    "You are not authorized to view this order."
            );


            return "redirect:/Krishna/orders";
        }


        // -------------------------------------------------
        // SEND ORDER TO PAGE
        // -------------------------------------------------

        model.addAttribute(
                "order",
                order
        );


        return "customer/order-details";
    }


    // =====================================================
    // CANCEL ORDER
    // =====================================================

    @PostMapping("/Krishna/orders/{orderId}/cancel")
    public String cancelOrder(
            @PathVariable Long orderId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {


        // -------------------------------------------------
        // GET LOGGED-IN CUSTOMER
        // -------------------------------------------------

        Customer customer =
                (Customer) session.getAttribute(
                        "loggedInCustomer"
                );


        // -------------------------------------------------
        // LOGIN CHECK
        // -------------------------------------------------

        if (customer == null) {

            return "redirect:/login";
        }


        Long customerId =
                customer.getId();


        // -------------------------------------------------
        // FIND ORDER
        // -------------------------------------------------

        Order order =
                orderService.getOrderById(orderId);


        // -------------------------------------------------
        // ORDER NOT FOUND
        // -------------------------------------------------

        if (order == null) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Order not found."
            );

            return "redirect:/Krishna/orders";
        }


        // -------------------------------------------------
        // SECURITY CHECK
        // -------------------------------------------------

        if (order.getCustomer() == null
                || order.getCustomer().getId() == null
                || !order.getCustomer()
                        .getId()
                        .equals(customerId)) {


            redirectAttributes.addFlashAttribute(
                    "error",
                    "You are not authorized to cancel this order."
            );


            return "redirect:/Krishna/orders";
        }


        // -------------------------------------------------
        // CANCEL ORDER
        // -------------------------------------------------

        try {

            orderService.cancelOrder(orderId);


            redirectAttributes.addFlashAttribute(
                    "success",
                    "Order cancelled successfully."
            );


        } catch (RuntimeException e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage()
            );
        }


        // -------------------------------------------------
        // RETURN TO DETAILS
        // -------------------------------------------------

        return "redirect:/Krishna/orders/" + orderId;
    }

    
}