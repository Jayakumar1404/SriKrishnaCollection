package com.example.srikrishna.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.srikrishna.Entity.CancellationRequest;

public interface CancellationRequestRepository
        extends JpaRepository<CancellationRequest, Long> {

    List<CancellationRequest>
    findAllByOrderByRequestedAtDesc();

    List<CancellationRequest>
    findByCustomerIdOrderByRequestedAtDesc(
            Long customerId
    );

    Optional<CancellationRequest>
    findByOrderId(Long orderId);

    boolean existsByOrderId(Long orderId);

    List<CancellationRequest>
    findByStatusOrderByRequestedAtDesc(
            String status
    );

    List<CancellationRequest>
    findByRefundStatusOrderByRefundDueDateAsc(
            String refundStatus
    );
}