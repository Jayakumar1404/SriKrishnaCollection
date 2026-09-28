package com.example.srikrishna.ServiceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.srikrishna.Entity.Payment;
import com.example.srikrishna.Repository.PaymentRepository;
import com.example.srikrishna.Service.AdminReportService;

@Service
public class AdminReportServiceImpl
        implements AdminReportService {

    private final PaymentRepository paymentRepository;

    public AdminReportServiceImpl(
            PaymentRepository paymentRepository) {

        this.paymentRepository = paymentRepository;
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAllByOrderByIdDesc();
    }

    @Override
    public long getTotalPayments() {
        return paymentRepository.count();
    }

    @Override
    public long getSuccessfulPayments() {
        return paymentRepository.countByPaymentStatus("SUCCESS");
    }

    @Override
    public long getPendingPayments() {
        return paymentRepository.countByPaymentStatus("PENDING");
    }

    @Override
    public long getFailedPayments() {
        return paymentRepository.countByPaymentStatus("FAILED");
    }

    @Override
    public long getCodPayments() {
        return paymentRepository.countByPaymentMethod("COD");
    }

    @Override
    public long getRazorpayPayments() {
        return paymentRepository.countByPaymentMethod("RAZORPAY");
    }

    @Override
    public Double getSuccessfulRevenue() {

        Double revenue =
                paymentRepository.getSuccessfulRevenue();

        return revenue == null ? 0.0 : revenue;
    }
}