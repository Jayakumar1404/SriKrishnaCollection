package com.example.srikrishna.Entity;

import jakarta.persistence.*;

@Entity
@Table(name="address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    private String mobile;

    private String address;

    private String city;

    private String state;

    private String pincode;

    @ManyToOne
    @JoinColumn(name="customer_id")
    private Customer customer;

    // Constructors
    // Getters and Setters
}