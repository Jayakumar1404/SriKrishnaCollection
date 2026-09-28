package com.example.srikrishna.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.srikrishna.Entity.InventoryHistory;

public interface InventoryHistoryRepository
        extends JpaRepository<InventoryHistory, Long> {

    List<InventoryHistory>
    findAllByOrderByCreatedAtDesc();

    List<InventoryHistory>
    findByProductIdOrderByCreatedAtDesc(Long productId);
}