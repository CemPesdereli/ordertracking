package com.cem.ordertracking.entity;

import com.cem.ordertracking.security.Customer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "order_info")
public class OrderInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)  // Many-to-One with Customer
    private Customer customer;

    private String orderStatus;
    private LocalDate estimatedDeliveryDate;
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "orderInfo", cascade = CascadeType.ALL)  // One-to-Many with OrderItem
    @JsonIgnore
    private List<OrderItem> orderItems;  // No need to initialize

    // Getters, setters, and other relevant methods (you can use Lombok for brevity)

    public OrderInfo(){
    }

    public OrderInfo(Customer customer, String orderStatus, LocalDate estimatedDeliveryDate) {
        this.customer = customer;
        this.orderStatus = orderStatus;
        this.estimatedDeliveryDate = estimatedDeliveryDate;
        this.createdAt=LocalDateTime.now();
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

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDate getEstimatedDeliveryDate() {
        return estimatedDeliveryDate;
    }

    public void setEstimatedDeliveryDate(LocalDate estimatedDeliveryDate) {
        this.estimatedDeliveryDate = estimatedDeliveryDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    @Override
    public String toString() {
        return "OrderInfo{" +
                "id=" + id +
                ", customer=" + customer +
                ", orderStatus='" + orderStatus + '\'' +
                ", estimatedDeliveryDate=" + estimatedDeliveryDate +
                ", createdAt=" + createdAt +
                ", orderItems=" + orderItems +
                '}';
    }
}