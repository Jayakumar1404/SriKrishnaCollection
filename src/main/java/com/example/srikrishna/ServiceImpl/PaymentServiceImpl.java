package com.example.srikrishna.ServiceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.srikrishna.Entity.Payment;
import com.example.srikrishna.Repository.PaymentRepository;
import com.example.srikrishna.Service.PaymentService;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public PaymentServiceImpl(
            PaymentRepository paymentRepository) {

        this.paymentRepository = paymentRepository;
    }

    // =====================================================
    // SAVE PAYMENT
    // =====================================================

    @Override
    public Payment save(Payment payment) {

        return paymentRepository.save(payment);
    }

    // =====================================================
    // GET ALL PAYMENTS
    // =====================================================

    @Override
    public List<Payment> getAllPayments() {

        return paymentRepository
                .findAllByOrderByIdDesc();
    }

    // =====================================================
    // GET PAYMENT BY ID
    // =====================================================

    @Override
    public Payment getPaymentById(Long id) {

        return paymentRepository
                .findById(id)
                .orElse(null);
    }

    // =====================================================
    // GET PAYMENTS BY STATUS
    // =====================================================

    @Override
    public List<Payment> getPaymentsByStatus(
            String status) {

        return paymentRepository
                .findByPaymentStatusOrderByIdDesc(status);
    }

    // =====================================================
    // GET PAYMENTS BY METHOD
    // =====================================================

    @Override
    public List<Payment> getPaymentsByMethod(
            String method) {

        return paymentRepository
                .findByPaymentMethodOrderByIdDesc(method);
    }

    // =====================================================
    // CHECK DUPLICATE RAZORPAY PAYMENT
    // =====================================================

    @Override
    public boolean paymentExists(
            String razorpayPaymentId) {

        if (razorpayPaymentId == null
                || razorpayPaymentId.trim().isEmpty()) {

            return false;
        }

        return paymentRepository
                .existsByRazorpayPaymentId(
                        razorpayPaymentId
                );
    }

    // =====================================================
    // TOTAL PAYMENTS
    // =====================================================

    @Override
    public long countAll() {

        return paymentRepository.count();
    }

    // =====================================================
    // SUCCESS PAYMENTS
    // =====================================================

    @Override
    public long countSuccess() {

        return paymentRepository
                .countByPaymentStatus("SUCCESS");
    }

    // =====================================================
    // PENDING PAYMENTS
    // =====================================================

    @Override
    public long countPending() {

        return paymentRepository
                .countByPaymentStatus("PENDING");
    }

    // =====================================================
    // FAILED PAYMENTS
    // =====================================================

    @Override
    public long countFailed() {

        return paymentRepository
                .countByPaymentStatus("FAILED");
    }

    // =====================================================
    // COD PAYMENTS
    // =====================================================

    @Override
    public long countCod() {

        return paymentRepository
                .countByPaymentMethod("COD");
    }

    // =====================================================
    // RAZORPAY PAYMENTS
    // =====================================================

    @Override
    public long countRazorpay() {

        return paymentRepository
                .countByPaymentMethod("RAZORPAY");
    }
}