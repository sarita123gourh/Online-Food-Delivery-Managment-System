package com.db.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    private String customer;
    private Double totalAmount;

    @ManyToOne
    private Users user;

    @ManyToMany
    @JsonIgnore // Prevent infinite recursion
    private List<MenuItem> menuItems;  // Menu items associated with the order

    public Order(long l, String s, String pending) {
    }

    public Order(Long orderId, String customer, Double totalAmount, Users user, List<MenuItem> menuItems) {
        this.orderId = orderId;
        this.customer = customer;
        this.totalAmount = totalAmount;
        this.user = user;
        this.menuItems = menuItems;
    }

    public Order() {

    }

    // Getters and Setters
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }
}
