package com.example.srikrishna.Service;

import java.util.List;

import com.example.srikrishna.Entity.Payment;

public interface PaymentService {


    // =====================================================
    // SAVE
    // =====================================================

    Payment save(Payment payment);


    // =====================================================
    // GET ALL
    // =====================================================

    List<Payment> getAllPayments();


    // =====================================================
    // GET BY ID
    // =====================================================

    Payment getPaymentById(Long id);


    // =====================================================
    // FILTER STATUS
    // =====================================================

    List<Payment> getPaymentsByStatus(
            String status
    );


    // =====================================================
    // FILTER METHOD
    // =====================================================

    List<Payment> getPaymentsByMethod(
            String method
    );


    // =====================================================
    // DUPLICATE PAYMENT CHECK
    // =====================================================

    boolean paymentExists(
            String razorpayPaymentId
    );


    // =====================================================
    // COUNTS
    // =====================================================

    long countAll();

    long countSuccess();

    long countPending();

    long countFailed();

    long countCod();

    long countRazorpay();
}