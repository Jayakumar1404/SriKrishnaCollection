package com.example.srikrishna.Service;

import java.util.List;

import com.example.srikrishna.Entity.CancellationRequest;

public interface CancellationService {

    CancellationRequest cancelOrder(
            Long orderId,
            Long customerId,
            String reason
    );

    List<CancellationRequest>
    getCustomerCancellations(Long customerId);

    List<CancellationRequest>
    getAllCancellations();

    CancellationRequest
    getByOrderId(Long orderId);
}