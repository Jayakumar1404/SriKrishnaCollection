package com.example.srikrishna.Service;

import java.util.List;

import com.example.srikrishna.Entity.InventoryHistory;
import com.example.srikrishna.Entity.Product;

public interface InventoryService {

    // =====================================================
    // PRODUCTS
    // =====================================================

    List<Product> getAllProducts();

    // =====================================================
    // STOCK UPDATE
    // =====================================================

    Product updateStock(
            Long productId,
            Integer quantity,
            String changeType,
            String reason
    );

    // =====================================================
    // INVENTORY HISTORY
    // =====================================================

    List<InventoryHistory> getHistory();

    List<InventoryHistory> getProductHistory(
            Long productId
    );

    // =====================================================
    // COUNTS
    // =====================================================

    long getTotalProducts();

    long getInStockProducts();

    long getLowStockProducts();

    long getOutOfStockProducts();
}