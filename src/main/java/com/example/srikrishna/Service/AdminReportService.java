package com.example.srikrishna.Service;

import java.util.List;

import com.example.srikrishna.Entity.Payment;

public interface AdminReportService {

    List<Payment> getAllPayments();

    long getTotalPayments();

    long getSuccessfulPayments();

    long getPendingPayments();

    long getFailedPayments();

    long getCodPayments();

    long getRazorpayPayments();

    Double getSuccessfulRevenue();
}