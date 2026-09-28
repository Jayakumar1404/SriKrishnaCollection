package com.example.srikrishna.ServiceImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.srikrishna.Entity.Customer;
import com.example.srikrishna.Entity.Product;
import com.example.srikrishna.Entity.Wishlist;
import com.example.srikrishna.Repository.CustomerRepository;
import com.example.srikrishna.Repository.ProductRepository;
import com.example.srikrishna.Repository.WishlistRepository;
import com.example.srikrishna.Service.WishlistService;

import jakarta.transaction.Transactional;

@Service
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;

    private final CustomerRepository customerRepository;

    private final ProductRepository productRepository;


    public WishlistServiceImpl(
            WishlistRepository wishlistRepository,
            CustomerRepository customerRepository,
            ProductRepository productRepository) {

        this.wishlistRepository = wishlistRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }


    // =========================================================
    // GET CUSTOMER WISHLIST
    // =========================================================

    @Override
    public List<Wishlist> getCustomerWishlist(
            Long customerId) {

        return wishlistRepository
                .findByCustomerId(customerId);
    }


    // =========================================================
    // GET WISHLIST PRODUCT IDs
    // =========================================================

    @Override
    public Set<Long> getWishlistProductIds(
            Long customerId) {

        List<Wishlist> wishlistItems =
                wishlistRepository
                        .findByCustomerId(customerId);

        Set<Long> productIds =
                new HashSet<>();

        for (Wishlist wishlist : wishlistItems) {

            if (wishlist.getProduct() != null
                    && wishlist.getProduct().getId() != null) {

                productIds.add(
                        wishlist.getProduct().getId()
                );
            }
        }

        return productIds;
    }


    // =========================================================
    // CHECK PRODUCT IN WISHLIST
    // =========================================================

    @Override
    public boolean isInWishlist(
            Long customerId,
            Long productId) {

        Wishlist wishlist =
                wishlistRepository
                        .findByCustomerIdAndProductId(
                                customerId,
                                productId
                        );

        return wishlist != null;
    }


    // =========================================================
    // ADD TO WISHLIST
    // =========================================================

    @Override
    @Transactional
    public void addToWishlist(
            Long customerId,
            Long productId) {

        // -----------------------------------------------------
        // CUSTOMER
        // -----------------------------------------------------

        Customer customer =
                customerRepository
                        .findById(customerId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Customer not found."
                                )
                        );


        // -----------------------------------------------------
        // CUSTOMER STATUS
        // -----------------------------------------------------

        if (customer.getStatus() == null
                || !customer.getStatus()) {

            throw new RuntimeException(
                    "Customer account is inactive."
            );
        }


        // -----------------------------------------------------
        // PRODUCT
        // -----------------------------------------------------

        Product product =
                productRepository
                        .findById(productId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Product not found."
                                )
                        );


        // -----------------------------------------------------
        // CHECK EXISTING WISHLIST
        // -----------------------------------------------------

        Wishlist existing =
                wishlistRepository
                        .findByCustomerIdAndProductId(
                                customerId,
                                productId
                        );


        if (existing != null) {

            throw new RuntimeException(
                    "Product is already in your wishlist."
            );
        }


        // -----------------------------------------------------
        // CREATE WISHLIST ITEM
        // -----------------------------------------------------

        Wishlist wishlist =
                new Wishlist();

        wishlist.setCustomer(
                customer
        );

        wishlist.setProduct(
                product
        );


        // -----------------------------------------------------
        // SAVE
        // -----------------------------------------------------

        wishlistRepository.save(
                wishlist
        );
    }


    // =========================================================
    // REMOVE FROM WISHLIST
    // =========================================================

    @Override
    @Transactional
    public void removeFromWishlist(
            Long customerId,
            Long productId) {

        // -----------------------------------------------------
        // CUSTOMER
        // -----------------------------------------------------

        Customer customer =
                customerRepository
                        .findById(customerId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Customer not found."
                                )
                        );


        // -----------------------------------------------------
        // FIND CUSTOMER'S WISHLIST ITEM
        // -----------------------------------------------------

        Wishlist wishlist =
                wishlistRepository
                        .findByCustomerIdAndProductId(
                                customerId,
                                productId
                        );


        if (wishlist == null) {

            throw new RuntimeException(
                    "Product is not in your wishlist."
            );
        }


        // -----------------------------------------------------
        // OWNERSHIP CHECK
        // -----------------------------------------------------

        if (wishlist.getCustomer() == null
                || wishlist.getCustomer().getId() == null
                || !wishlist.getCustomer()
                        .getId()
                        .equals(customer.getId())) {

            throw new RuntimeException(
                    "You are not authorized to remove this item."
            );
        }


        // -----------------------------------------------------
        // DELETE
        // -----------------------------------------------------

        wishlistRepository.delete(
                wishlist
        );
    }


    // =========================================================
    // CLEAR CUSTOMER WISHLIST
    // =========================================================

    @Override
    @Transactional
    public void clearWishlist(
            Long customerId) {

        // -----------------------------------------------------
        // VERIFY CUSTOMER
        // -----------------------------------------------------

        customerRepository
                .findById(customerId)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Customer not found."
                        )
                );


        // -----------------------------------------------------
        // DELETE ONLY THIS CUSTOMER'S WISHLIST
        // -----------------------------------------------------

        wishlistRepository.deleteByCustomerId(
                customerId
        );
    }
}