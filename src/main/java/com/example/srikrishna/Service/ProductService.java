package com.example.srikrishna.Service;

import java.util.List;

import com.example.srikrishna.Entity.Product;

public interface ProductService {

    Product saveProduct(Product product);

    List<Product> getAllProducts();

    Product getProductById(Long id);

    Product updateProduct(Product product);

    void deleteProduct(Long productId);

    List<Product> getActiveProducts();

    List<Product> getProductsByCategory(String categoryName);
}