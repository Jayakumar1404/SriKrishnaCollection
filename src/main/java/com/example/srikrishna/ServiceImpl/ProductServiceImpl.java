package com.example.srikrishna.ServiceImpl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.srikrishna.Entity.Product;
import com.example.srikrishna.Repository.CartRepository;
import com.example.srikrishna.Repository.ProductRepository;
import com.example.srikrishna.Service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    public ProductServiceImpl(
            ProductRepository productRepository,
            CartRepository cartRepository) {

        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
    }

    // =====================================================
    // SAVE PRODUCT
    // =====================================================

    @Override
    public Product saveProduct(Product product) {

        return productRepository.save(product);
    }

    // =====================================================
    // GET ALL PRODUCTS - ADMIN
    // =====================================================

    @Override
    public List<Product> getAllProducts() {

        return productRepository.findAll();
    }

    // =====================================================
    // GET PRODUCT BY ID
    // =====================================================

    @Override
    public Product getProductById(Long id) {

        return productRepository
                .findById(id)
                .orElse(null);
    }

    // =====================================================
    // UPDATE PRODUCT
    // =====================================================

    @Override
    public Product updateProduct(Product product) {

        return productRepository.save(product);
    }

    // =====================================================
    // DELETE PRODUCT
    // =====================================================

    @Override
    @Transactional
    public void deleteProduct(Long productId) {

        Product product = productRepository
                .findById(productId)
                .orElse(null);

        if (product == null) {
            return;
        }

        // Remove product from carts
        cartRepository.deleteByProductId(productId);

        // Soft delete
        product.setStatus(false);

        productRepository.save(product);

        System.out.println(
                "PRODUCT SOFT DELETED: ID = "
                        + productId
                        + " STATUS = "
                        + product.getStatus());
    }

    // =====================================================
    // GET PRODUCTS BY CATEGORY - CUSTOMER
    // =====================================================

    @Override
    public List<Product> getProductsByCategory(String categoryName) {

        return productRepository
                .findByCategory_NameIgnoreCaseAndStatusTrue(
                        categoryName);
    }

    // =====================================================
    // GET ALL ACTIVE PRODUCTS - CUSTOMER
    // =====================================================

    @Override
    public List<Product> getActiveProducts() {

        return productRepository.findByStatusTrue();
    }
}