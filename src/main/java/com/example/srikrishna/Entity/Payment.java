package com.example.srikrishna.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // =====================================================
    // ORDER
    // =====================================================

    private Long orderId;


    // =====================================================
    // PAYMENT METHOD
    // COD / RAZORPAY
    // =====================================================

    private String paymentMethod;


    // =====================================================
    // AMOUNT
    // =====================================================

    private Double amount;


    // =====================================================
    // PAYMENT STATUS
    // SUCCESS / PENDING / FAILED
    // =====================================================

    private String paymentStatus;


    // =====================================================
    // TRANSACTION ID
    // =====================================================

    private String transactionId;


    // =====================================================
    // RAZORPAY ORDER ID
    // =====================================================

    private String razorpayOrderId;


    // =====================================================
    // RAZORPAY PAYMENT ID
    // =====================================================

    private String razorpayPaymentId;


    // =====================================================
    // RAZORPAY SIGNATURE
    // =====================================================

    private String razorpaySignature;


    // =====================================================
    // CREATED DATE
    // =====================================================

    private LocalDateTime createdAt;


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public Payment() {

        this.createdAt =
                LocalDateTime.now();
    }


    // =====================================================
    // GETTERS / SETTERS
    // =====================================================

    public Long getId() {

        return id;
    }


    public void setId(Long id) {

        this.id = id;
    }


    public Long getOrderId() {

        return orderId;
    }


    public void setOrderId(Long orderId) {

        this.orderId = orderId;
    }


    public String getPaymentMethod() {

        return paymentMethod;
    }


    public void setPaymentMethod(
            String paymentMethod) {

        this.paymentMethod =
                paymentMethod;
    }


    public Double getAmount() {

        return amount;
    }


    public void setAmount(Double amount) {

        this.amount =
                amount;
    }


    public String getPaymentStatus() {

        return paymentStatus;
    }


    public void setPaymentStatus(
            String paymentStatus) {

        this.paymentStatus =
                paymentStatus;
    }


    public String getTransactionId() {

        return transactionId;
    }


    public void setTransactionId(
            String transactionId) {

        this.transactionId =
                transactionId;
    }


    public String getRazorpayOrderId() {

        return razorpayOrderId;
    }


    public void setRazorpayOrderId(
            String razorpayOrderId) {

        this.razorpayOrderId =
                razorpayOrderId;
    }


    public String getRazorpayPaymentId() {

        return razorpayPaymentId;
    }


    public void setRazorpayPaymentId(
            String razorpayPaymentId) {

        this.razorpayPaymentId =
                razorpayPaymentId;
    }


    public String getRazorpaySignature() {

        return razorpaySignature;
    }


    public void setRazorpaySignature(
            String razorpaySignature) {

        this.razorpaySignature =
                razorpaySignature;
    }


    public LocalDateTime getCreatedAt() {

        return createdAt;
    }


    public void setCreatedAt(
            LocalDateTime createdAt) {

        this.createdAt =
                createdAt;
    }
}