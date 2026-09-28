package com.example.srikrishna.ServiceImpl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.srikrishna.Entity.InventoryHistory;
import com.example.srikrishna.Entity.Product;
import com.example.srikrishna.Repository.InventoryHistoryRepository;
import com.example.srikrishna.Repository.ProductRepository;
import com.example.srikrishna.Service.InventoryService;

@Service
public class InventoryServiceImpl
        implements InventoryService {

    // =====================================================
    // LOW STOCK LIMIT
    // =====================================================

    private static final int LOW_STOCK_LIMIT = 5;

    private final ProductRepository productRepository;

    private final InventoryHistoryRepository
            inventoryHistoryRepository;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public InventoryServiceImpl(
            ProductRepository productRepository,
            InventoryHistoryRepository inventoryHistoryRepository) {

        this.productRepository =
                productRepository;

        this.inventoryHistoryRepository =
                inventoryHistoryRepository;
    }

    // =====================================================
    // GET ALL PRODUCTS
    // =====================================================

    @Override
    public List<Product> getAllProducts() {

        return productRepository.findAll();
    }

    // =====================================================
    // UPDATE STOCK
    // =====================================================

    @Override
    @Transactional
    public Product updateStock(
            Long productId,
            Integer quantity,
            String changeType,
            String reason) {

        if (quantity == null || quantity <= 0) {

            throw new RuntimeException(
                    "Quantity must be greater than zero."
            );
        }

        Product product =
                productRepository
                        .findById(productId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Product not found."
                                )
                        );

        Integer currentStock =
                product.getStock();

        if (currentStock == null) {
            currentStock = 0;
        }

        int newStock;

        // =================================================
        // STOCK IN
        // =================================================

        if ("STOCK_IN".equalsIgnoreCase(changeType)) {

            newStock =
                    currentStock + quantity;
        }

        // =================================================
        // STOCK OUT
        // =================================================

        else if ("STOCK_OUT".equalsIgnoreCase(changeType)) {

            if (quantity > currentStock) {

                throw new RuntimeException(
                        "Stock cannot become negative."
                );
            }

            newStock =
                    currentStock - quantity;
        }

        // =================================================
        // ADJUSTMENT
        // =================================================

        else {

            throw new RuntimeException(
                    "Invalid stock change type."
            );
        }

        // =================================================
        // SAVE PRODUCT STOCK
        // =================================================

        product.setStock(newStock);

        Product savedProduct =
                productRepository.save(product);

        // =================================================
        // SAVE HISTORY
        // =================================================

        InventoryHistory history =
                new InventoryHistory();

        history.setProduct(product);

        history.setQuantityChange(
                "STOCK_IN".equalsIgnoreCase(changeType)
                        ? quantity
                        : -quantity
        );

        history.setStockBefore(
                currentStock
        );

        history.setStockAfter(
                newStock
        );

        history.setChangeType(
                changeType.toUpperCase()
        );

        history.setReason(reason);

        inventoryHistoryRepository.save(
                history
        );

        return savedProduct;
    }

    // =====================================================
    // ALL HISTORY
    // =====================================================

    @Override
    public List<InventoryHistory> getHistory() {

        return inventoryHistoryRepository
                .findAllByOrderByCreatedAtDesc();
    }

    // =====================================================
    // PRODUCT HISTORY
    // =====================================================

    @Override
    public List<InventoryHistory>
    getProductHistory(Long productId) {

        return inventoryHistoryRepository
                .findByProductIdOrderByCreatedAtDesc(
                        productId
                );
    }

    // =====================================================
    // TOTAL PRODUCTS
    // =====================================================

    @Override
    public long getTotalProducts() {

        return productRepository.count();
    }

    // =====================================================
    // IN STOCK
    // =====================================================

    @Override
    public long getInStockProducts() {

        return productRepository
                .findAll()
                .stream()
                .filter(p ->
                        p.getStock() != null
                                && p.getStock() > LOW_STOCK_LIMIT
                )
                .count();
    }

    // =====================================================
    // LOW STOCK
    // =====================================================

    @Override
    public long getLowStockProducts() {

        return productRepository
                .findAll()
                .stream()
                .filter(p ->
                        p.getStock() != null
                                && p.getStock() > 0
                                && p.getStock()
                                    <= LOW_STOCK_LIMIT
                )
                .count();
    }

    // =====================================================
    // OUT OF STOCK
    // =====================================================

    @Override
    public long getOutOfStockProducts() {

        return productRepository
                .findAll()
                .stream()
                .filter(p ->
                        p.getStock() == null
                                || p.getStock() <= 0
                )
                .count();
    }
}