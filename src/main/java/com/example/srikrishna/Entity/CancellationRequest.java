package com.example.srikrishna.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "cancellation_requests")
public class CancellationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =====================================================
    // ORDER
    // =====================================================

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // =====================================================
    // CUSTOMER
    // =====================================================

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    // =====================================================
    // REASON
    // =====================================================

    @Column(length = 500)
    private String reason;

    // =====================================================
    // STATUS
    // PENDING / APPROVED / REJECTED
    // =====================================================

    private String status;

    // =====================================================
    // REQUEST DATE
    // =====================================================

    private LocalDateTime requestedAt;

    // =====================================================
    // PROCESSED DATE
    // =====================================================

    private LocalDateTime processedAt;

    // =====================================================
    // REFUND STATUS
    // NONE / REFUND_PENDING / REFUNDED
    // =====================================================

    private String refundStatus;

    // =====================================================
    // REFUND DUE DATE
    // =====================================================

    private LocalDateTime refundDueDate;

    // =====================================================
    // CREATED DATE
    // =====================================================

    @PrePersist
    public void prePersist() {

        if (requestedAt == null) {
            requestedAt = LocalDateTime.now();
        }

        if (status == null) {
            status = "PENDING";
        }

        if (refundStatus == null) {
            refundStatus = "NONE";
        }
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

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(LocalDateTime requestedAt) {
        this.requestedAt = requestedAt;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    public LocalDateTime getRefundDueDate() {
        return refundDueDate;
    }

    public void setRefundDueDate(LocalDateTime refundDueDate) {
        this.refundDueDate = refundDueDate;
    }
}