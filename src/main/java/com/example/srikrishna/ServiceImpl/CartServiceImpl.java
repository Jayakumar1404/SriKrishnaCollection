package com.example.srikrishna.ServiceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.srikrishna.Entity.Cart;
import com.example.srikrishna.Entity.Customer;
import com.example.srikrishna.Entity.Product;
import com.example.srikrishna.Repository.CartRepository;
import com.example.srikrishna.Repository.CustomerRepository;
import com.example.srikrishna.Repository.ProductRepository;
import com.example.srikrishna.Service.CartService;

import jakarta.transaction.Transactional;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    private final CustomerRepository customerRepository;

    private final ProductRepository productRepository;


    public CartServiceImpl(
            CartRepository cartRepository,
            CustomerRepository customerRepository,
            ProductRepository productRepository) {

        this.cartRepository = cartRepository;

        this.customerRepository = customerRepository;

        this.productRepository = productRepository;
    }


    // =========================================================
    // GET CUSTOMER CART
    // =========================================================

    @Override
    public List<Cart> getCustomerCart(
            Long customerId) {

        return cartRepository.findByCustomerId(
                customerId
        );
    }


    // =========================================================
    // CART TOTAL
    // =========================================================

    @Override
    public Double getCartTotal(
            Long customerId) {

        List<Cart> cartItems =
                cartRepository.findByCustomerId(
                        customerId
                );

        double total = 0.0;

        for (Cart cart : cartItems) {

            if (cart.getTotalPrice() != null) {

                total += cart.getTotalPrice();
            }
        }

        return total;
    }


    // =========================================================
    // ADD TO CART
    // =========================================================

    @Override
    @Transactional
    public void addToCart(
            Long customerId,
            Long productId,
            int quantity) {

        // -----------------------------------------------------
        // QUANTITY VALIDATION
        // -----------------------------------------------------

        if (quantity <= 0) {

            throw new RuntimeException(
                    "Quantity must be greater than 0."
            );
        }


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
        // STOCK CHECK
        // -----------------------------------------------------

        if (product.getStock() == null
                || product.getStock() <= 0) {

            throw new RuntimeException(
                    "Product is out of stock."
            );
        }


        if (quantity > product.getStock()) {

            throw new RuntimeException(
                    "Not enough stock available."
            );
        }


        // -----------------------------------------------------
        // CHECK EXISTING CART ITEM
        // -----------------------------------------------------

        Cart cart =
                cartRepository
                        .findByCustomerIdAndProductId(
                                customerId,
                                productId
                        );


        // =====================================================
        // EXISTING CART ITEM
        // =====================================================

        if (cart != null) {

            int newQuantity =
                    cart.getQuantity()
                            + quantity;


            if (newQuantity > product.getStock()) {

                throw new RuntimeException(
                        "Only "
                                + product.getStock()
                                + " items available."
                );
            }


            cart.setQuantity(
                    newQuantity
            );


            cart.setTotalPrice(
                    product.getPrice()
                            * newQuantity
            );
        }


        // =====================================================
        // NEW CART ITEM
        // =====================================================

        else {

            cart = new Cart();

            cart.setCustomer(
                    customer
            );

            cart.setProduct(
                    product
            );

            cart.setQuantity(
                    quantity
            );

            cart.setTotalPrice(
                    product.getPrice()
                            * quantity
            );
        }


        // -----------------------------------------------------
        // SAVE
        // -----------------------------------------------------

        cartRepository.save(cart);
    }


    // =========================================================
    // UPDATE QUANTITY
    // =========================================================

    @Override
    @Transactional
    public void updateQuantity(
            Long customerId,
            Long cartId,
            int quantity) {

        // -----------------------------------------------------
        // QUANTITY VALIDATION
        // -----------------------------------------------------

        if (quantity <= 0) {

            throw new RuntimeException(
                    "Quantity must be greater than 0."
            );
        }


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
        // CART
        // -----------------------------------------------------

        Cart cart =
                cartRepository
                        .findById(cartId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Cart item not found."
                                )
                        );


        // =====================================================
        // OWNERSHIP CHECK
        // =====================================================

        if (cart.getCustomer() == null
                || cart.getCustomer().getId() == null
                || !cart.getCustomer()
                        .getId()
                        .equals(customer.getId())) {

            throw new RuntimeException(
                    "You are not authorized to modify this cart item."
            );
        }


        // -----------------------------------------------------
        // PRODUCT
        // -----------------------------------------------------

        Product product =
                cart.getProduct();


        if (product == null) {

            throw new RuntimeException(
                    "Product not found."
            );
        }


        // -----------------------------------------------------
        // STOCK
        // -----------------------------------------------------

        if (product.getStock() != null
                && quantity > product.getStock()) {

            throw new RuntimeException(
                    "Only "
                            + product.getStock()
                            + " items available."
            );
        }


        // -----------------------------------------------------
        // UPDATE
        // -----------------------------------------------------

        cart.setQuantity(
                quantity
        );


        cart.setTotalPrice(
                product.getPrice()
                        * quantity
        );


        cartRepository.save(cart);
    }


    // =========================================================
    // REMOVE FROM CART
    // =========================================================

    @Override
    @Transactional
    public void removeFromCart(
            Long customerId,
            Long cartId) {

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
        // CART
        // -----------------------------------------------------

        Cart cart =
                cartRepository
                        .findById(cartId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Cart item not found."
                                )
                        );


        // =====================================================
        // OWNERSHIP CHECK
        // =====================================================

        if (cart.getCustomer() == null
                || cart.getCustomer().getId() == null
                || !cart.getCustomer()
                        .getId()
                        .equals(customer.getId())) {

            throw new RuntimeException(
                    "You are not authorized to remove this cart item."
            );
        }


        // -----------------------------------------------------
        // DELETE
        // -----------------------------------------------------

        cartRepository.delete(cart);
    }


    // =========================================================
    // CLEAR CART
    // =========================================================

    @Override
    @Transactional
    public void clearCart(
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
        // DELETE ONLY THIS CUSTOMER'S CART
        // -----------------------------------------------------

        cartRepository.deleteByCustomerId(
                customerId
        );
    }
}