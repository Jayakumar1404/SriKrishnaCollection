package com.example.srikrishna.Entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderItem> orderItems = new ArrayList<>();

    private Double totalAmount;

    private String paymentMethod;

    private String paymentStatus;

    private String orderStatus;

    @Column(length = 500)
    private String shippingAddress;

    private String city;

    private String state;

    private String pincode;

    private String phone;

    // IMPORTANT
    @Column(name = "order_date")
private LocalDateTime orderDate;

@PrePersist
public void prePersist() {

    if (orderDate == null) {

        orderDate = LocalDateTime.now();

    }
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


    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(
            List<OrderItem> orderItems) {

        this.orderItems = orderItems;
    }


    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(
            Double totalAmount) {

        this.totalAmount = totalAmount;
    }


    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(
            String paymentMethod) {

        this.paymentMethod = paymentMethod;
    }


    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(
            String paymentStatus) {

        this.paymentStatus = paymentStatus;
    }


    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(
            String orderStatus) {

        this.orderStatus = orderStatus;
    }


    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(
            String shippingAddress) {

        this.shippingAddress = shippingAddress;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {

        this.city = city;
    }


    public String getState() {
        return state;
    }

    public void setState(String state) {

        this.state = state;
    }


    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {

        this.pincode = pincode;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {

        this.phone = phone;
    }


    // IMPORTANT
    public LocalDateTime getOrderDate() {

        return orderDate;

    }


    public void setOrderDate(
            LocalDateTime orderDate) {

        this.orderDate = orderDate;

    }


    public void addOrderItem(
            OrderItem item) {

        orderItems.add(item);

        item.setOrder(this);
    }
}