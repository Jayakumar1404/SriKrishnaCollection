package com.example.srikrishna.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.srikrishna.Entity.Payment;

public interface PaymentRepository
        extends JpaRepository<Payment, Long> {

    List<Payment> findAllByOrderByIdDesc();

    List<Payment> findByPaymentStatusOrderByIdDesc(
            String paymentStatus);

    List<Payment> findByPaymentMethodOrderByIdDesc(
            String paymentMethod);

    Optional<Payment> findByRazorpayOrderId(
            String razorpayOrderId);

    Optional<Payment> findByRazorpayPaymentId(
            String razorpayPaymentId);

    boolean existsByRazorpayPaymentId(
            String razorpayPaymentId);
        Optional<Payment> findByOrderId(Long orderId);

    long countByPaymentStatus(
            String paymentStatus);

    long countByPaymentMethod(
            String paymentMethod);

    @Query("""
        SELECT COALESCE(SUM(p.amount), 0)
        FROM Payment p
        WHERE p.paymentStatus = 'SUCCESS'
    """)
    Double getSuccessfulRevenue();
}