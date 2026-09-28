package com.example.srikrishna.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_item")
public class OrderItem {

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
    // PRODUCT
    // =====================================================

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;


    // =====================================================
    // SNAPSHOT DATA
    // =====================================================

    private Integer quantity;

    private Double price;

    private Double totalPrice;


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public OrderItem() {
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


    public Product getProduct() {
        return product;
    }


    public void setProduct(Product product) {
        this.product = product;
    }


    public Integer getQuantity() {
        return quantity;
    }


    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }


    public Double getPrice() {
        return price;
    }


    public void setPrice(Double price) {
        this.price = price;
    }


    public Double getTotalPrice() {
        return totalPrice;
    }


    public void setTotalPrice(
            Double totalPrice) {

        this.totalPrice = totalPrice;
    }
}