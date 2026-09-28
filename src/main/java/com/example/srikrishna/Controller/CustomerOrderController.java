// package com.example.srikrishna.Controller;

// import com.example.srikrishna.Entity.Order;
// import com.example.srikrishna.Service.OrderService;

// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RequestMapping;

// @Controller
// @RequestMapping("/Krishna")
// public class CustomerOrderController {

//     private final OrderService orderService;

//     public CustomerOrderController(OrderService orderService) {
//         this.orderService = orderService;
//     }

//     // =====================================================
//     // CUSTOMER ORDER DETAILS
//     // =====================================================

//     @GetMapping("/orders/{orderId}")
//     public String orderDetails(
//             @PathVariable Long orderId,
//             Model model) {

//         Order order = orderService.getOrderById(orderId);

//         if (order == null) {

//             return "redirect:/Krishna/orders";

//         }

//         model.addAttribute("order", order);

//         return "customer/order-details";
//     }
// }