package com.example.srikrishna.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "wishlist",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"customer_id", "product_id"}
        )
    }
)
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(
        name = "customer_id",
        nullable = false
    )
    private Customer customer;


    @ManyToOne
    @JoinColumn(
        name = "product_id",
        nullable = false
    )
    private Product product;


    public Wishlist() {
    }


    public Wishlist(
            Long id,
            Customer customer,
            Product product) {

        this.id = id;
        this.customer = customer;
        this.product = product;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public Customer getCustomer() {
        return customer;
    }


    public void setCustomer(Customer customer) {
        this.customer = customer;
    }


    public Product getProduct() {
        return product;
    }


    public void setProduct(Product product) {
        this.product = product;
    }
}