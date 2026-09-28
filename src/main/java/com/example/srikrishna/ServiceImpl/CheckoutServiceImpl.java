package com.example.srikrishna.ServiceImpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.srikrishna.Entity.Cart;
import com.example.srikrishna.Entity.Customer;
import com.example.srikrishna.Entity.Order;
import com.example.srikrishna.Entity.OrderItem;
import com.example.srikrishna.Entity.Product;
import com.example.srikrishna.Repository.CartRepository;
import com.example.srikrishna.Repository.CustomerRepository;
import com.example.srikrishna.Repository.OrderRepository;
import com.example.srikrishna.Repository.ProductRepository;
import com.example.srikrishna.Service.CheckoutService;

import jakarta.transaction.Transactional;

@Service
public class CheckoutServiceImpl implements CheckoutService {

        private final CartRepository cartRepository;
        private final CustomerRepository customerRepository;
        private final ProductRepository productRepository;
        private final OrderRepository orderRepository;

        // =========================================================
        // CONSTRUCTOR
        // =========================================================

        public CheckoutServiceImpl(
                        CartRepository cartRepository,
                        CustomerRepository customerRepository,
                        ProductRepository productRepository,
                        OrderRepository orderRepository) {

                this.cartRepository = cartRepository;
                this.customerRepository = customerRepository;
                this.productRepository = productRepository;
                this.orderRepository = orderRepository;
        }

        // =========================================================
        // PLACE ORDER
        // =========================================================

        @Override
        @Transactional
        public Order placeOrder(

                        Long customerId,

                        String shippingAddress,

                        String city,

                        String state,

                        String pincode,

                        String phone,

                        String paymentMethod) {

                // =====================================================
                // CUSTOMER
                // =====================================================

                Customer customer = customerRepository
                                .findById(customerId)
                                .orElseThrow(
                                                () -> new RuntimeException(
                                                                "Customer not found."));

                // =====================================================
                // CUSTOMER STATUS
                // =====================================================

                if (customer.getStatus() == null
                                || !customer.getStatus()) {

                        throw new RuntimeException(
                                        "Customer account is inactive.");
                }

                // =====================================================
                // ADDRESS
                // =====================================================

                if (shippingAddress == null
                                || shippingAddress.trim().isEmpty()) {

                        throw new RuntimeException(
                                        "Shipping address is required.");
                }

                // =====================================================
                // GET CART
                // =====================================================

                List<Cart> cartItems = cartRepository.findByCustomerId(
                                customerId);

                if (cartItems == null
                                || cartItems.isEmpty()) {

                        throw new RuntimeException(
                                        "Your cart is empty.");
                }

                // =====================================================
                // CREATE ORDER
                // =====================================================

                Order order = new Order();

                // =====================================================
                // CREATE ORDER
                // =====================================================

             

                order.setCustomer(customer);

                order.setOrderDate(LocalDateTime.now());

                order.setShippingAddress(
                                shippingAddress.trim());

                order.setCity(city);

                order.setState(state);

                order.setPincode(pincode);

                order.setPhone(phone);

                // =====================================================
                // PAYMENT
                // =====================================================

                if (paymentMethod == null
                                || paymentMethod.trim().isEmpty()) {

                        paymentMethod = "COD";
                }

                order.setPaymentMethod(
                                paymentMethod);

                order.setPaymentStatus(
                                "PENDING");

                // =====================================================
                // ORDER STATUS
                // =====================================================

                order.setOrderStatus(
                                "PLACED");

                // =====================================================
                // ORDER DATE
                // =====================================================

                order.setOrderDate(
                                LocalDateTime.now());

                // =====================================================
                // TOTAL
                // =====================================================

                double totalAmount = 0.0;

                // =====================================================
                // ORDER ITEMS
                // =====================================================

                for (Cart cart : cartItems) {

                        if (cart.getProduct() == null) {

                                throw new RuntimeException(
                                                "Product not found in cart.");
                        }

                        Product product = productRepository
                                        .findById(
                                                        cart.getProduct().getId())
                                        .orElseThrow(
                                                        () -> new RuntimeException(
                                                                        "Product not found."));

                        // =================================================
                        // QUANTITY
                        // =================================================

                        if (cart.getQuantity() == null
                                        || cart.getQuantity() <= 0) {

                                throw new RuntimeException(
                                                "Invalid cart quantity.");
                        }

                        int quantity = cart.getQuantity();

                        // =================================================
                        // STOCK
                        // =================================================

                        if (product.getStock() == null
                                        || product.getStock() < quantity) {

                                throw new RuntimeException(
                                                "Not enough stock available for: "
                                                                + product.getName());
                        }

                        // =================================================
                        // PRICE
                        // =================================================

                        if (product.getPrice() == null
                                        || product.getPrice() < 0) {

                                throw new RuntimeException(
                                                "Invalid product price for: "
                                                                + product.getName());
                        }

                        double price = product.getPrice();

                        double itemTotal = price * quantity;

                        totalAmount += itemTotal;

                        // =================================================
                        // CREATE ORDER ITEM
                        // =================================================

                        OrderItem orderItem = new OrderItem();

                        orderItem.setProduct(product);

                        orderItem.setQuantity(quantity);

                        orderItem.setPrice(price);

                        orderItem.setTotalPrice(itemTotal);

                        // =================================================
                        // CONNECT ORDER AND ITEM
                        // =================================================

                        order.addOrderItem(
                                        orderItem);

                        // =================================================
                        // REDUCE STOCK
                        // =================================================

                        product.setStock(
                                        product.getStock() - quantity);

                        productRepository.save(product);
                }

                // =====================================================
                // VALIDATE TOTAL
                // =====================================================

                if (totalAmount <= 0) {

                        throw new RuntimeException(
                                        "Invalid order total.");
                }

                // =====================================================
                // SET TOTAL
                // =====================================================

                order.setTotalAmount(
                                totalAmount);

                // =====================================================
                // SAVE ORDER
                // =====================================================

                Order savedOrder = orderRepository.save(order);

                // =====================================================
                // CLEAR CART
                // =====================================================

                cartRepository.deleteByCustomerId(
                                customerId);

                // =====================================================
                // RETURN CREATED ORDER
                // =====================================================

                return savedOrder;
        }
}