package com.cem.ordertracking.entity;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import jakarta.persistence.*;

@Entity
@Table(name = "order_item")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)  // Many-to-One with OrderInfo
    @JsonIdentityReference(alwaysAsId = true)
    private OrderInfo orderInfo;

    @ManyToOne(fetch = FetchType.EAGER)  // Many-to-One with Product
    @JsonIdentityReference(alwaysAsId = true)
    private Product product;

    private int quantity;

    // Getters, setters, and other relevant methods (you can use Lombok for brevity)

    public OrderItem(){
    }

    public OrderItem(OrderInfo orderInfo, Product product, int quantity) {
        this.orderInfo = orderInfo;
        this.product = product;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", product=" + product +
                ", quantity=" + quantity +
                '}';
    }
}