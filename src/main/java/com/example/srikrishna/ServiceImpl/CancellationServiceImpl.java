package com.example.srikrishna.ServiceImpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.srikrishna.Entity.CancellationRequest;
import com.example.srikrishna.Entity.Customer;
import com.example.srikrishna.Entity.Order;
import com.example.srikrishna.Entity.Payment;
import com.example.srikrishna.Repository.CancellationRequestRepository;
import com.example.srikrishna.Repository.CustomerRepository;
import com.example.srikrishna.Repository.OrderRepository;
import com.example.srikrishna.Repository.PaymentRepository;
import com.example.srikrishna.Service.CancellationService;

@Service
public class CancellationServiceImpl
        implements CancellationService {

    private final CancellationRequestRepository
            cancellationRepository;

    private final OrderRepository orderRepository;

    private final CustomerRepository customerRepository;

    private final PaymentRepository paymentRepository;

    public CancellationServiceImpl(
            CancellationRequestRepository cancellationRepository,
            OrderRepository orderRepository,
            CustomerRepository customerRepository,
            PaymentRepository paymentRepository) {

        this.cancellationRepository =
                cancellationRepository;

        this.orderRepository =
                orderRepository;

        this.customerRepository =
                customerRepository;

        this.paymentRepository =
                paymentRepository;
    }

    // =====================================================
    // CUSTOMER CANCEL ORDER
    // =====================================================

    @Override
    @Transactional
    public CancellationRequest cancelOrder(
            Long orderId,
            Long customerId,
            String reason) {

        Order order =
                orderRepository
                        .findById(orderId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Order not found."
                                )
                        );

        Customer customer =
                customerRepository
                        .findById(customerId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Customer not found."
                                )
                        );

        // =================================================
        // VERIFY ORDER OWNER
        // =================================================

        if (order.getCustomer() == null
                || !order.getCustomer()
                        .getId()
                        .equals(customerId)) {

            throw new RuntimeException(
                    "You cannot cancel this order."
            );
        }

        // =================================================
        // CHECK DUPLICATE REQUEST
        // =================================================

        if (cancellationRepository
                .existsByOrderId(orderId)) {

            throw new RuntimeException(
                    "Cancellation request already exists."
            );
        }

        // =================================================
        // CHECK ORDER STATUS
        // =================================================

        String orderStatus =
                order.getOrderStatus();

        if ("CANCELLED".equalsIgnoreCase(orderStatus)) {

            throw new RuntimeException(
                    "Order is already cancelled."
            );
        }

        if ("DELIVERED".equalsIgnoreCase(orderStatus)) {

            throw new RuntimeException(
                    "Delivered orders cannot be cancelled."
            );
        }

        if ("SHIPPED".equalsIgnoreCase(orderStatus)) {

            throw new RuntimeException(
                    "Shipped orders cannot be cancelled."
            );
        }

        // =================================================
        // CREATE CANCELLATION REQUEST
        // =================================================

        CancellationRequest request =
                new CancellationRequest();

        request.setOrder(order);

        request.setCustomer(customer);

        request.setReason(reason);

        request.setStatus("PENDING");

        request.setRefundStatus("NONE");

        request.setRequestedAt(
                LocalDateTime.now()
        );

        // =================================================
        // CUSTOMER REQUEST
        // =================================================

        /*
         * We do NOT immediately process the refund.
         *
         * Admin can manually refund later,
         * or the automatic 3-day scheduler
         * can process it.
         */

        if ("RAZORPAY".equalsIgnoreCase(
                order.getPaymentMethod())) {

            request.setRefundStatus(
                    "REFUND_PENDING"
            );

            request.setRefundDueDate(
                    LocalDateTime.now()
                            .plusDays(3)
            );
        }

        // =================================================
        // CANCEL ORDER
        // =================================================

        order.setOrderStatus("CANCELLED");

        // =================================================
        // PAYMENT
        // =================================================

        Payment payment =
                paymentRepository
                        .findByOrderId(orderId)
                        .orElse(null);

        if (payment != null) {

            if ("RAZORPAY".equalsIgnoreCase(
                    payment.getPaymentMethod())) {

                payment.setPaymentStatus(
                        "REFUND_PENDING"
                );

            }
        }

        // =================================================
        // SAVE
        // =================================================

        orderRepository.save(order);

        if (payment != null) {
            paymentRepository.save(payment);
        }

        return cancellationRepository.save(
                request
        );
    }

    // =====================================================
    // CUSTOMER CANCELLATIONS
    // =====================================================

    @Override
    public List<CancellationRequest>
    getCustomerCancellations(
            Long customerId) {

        return cancellationRepository
                .findByCustomerIdOrderByRequestedAtDesc(
                        customerId
                );
    }

    // =====================================================
    // ALL CANCELLATIONS
    // =====================================================

    @Override
    public List<CancellationRequest>
    getAllCancellations() {

        return cancellationRepository
                .findAllByOrderByRequestedAtDesc();
    }

    // =====================================================
    // FIND BY ORDER
    // =====================================================

    @Override
    public CancellationRequest
    getByOrderId(Long orderId) {

        return cancellationRepository
                .findByOrderId(orderId)
                .orElse(null);
    }
}