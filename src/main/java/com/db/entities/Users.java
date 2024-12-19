package com.db.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "\"user\"")
public class Users {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long User_id;
    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;//CUSTOMER,RESTAURANT,ADMIN

    @OneToMany(mappedBy="user",cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonIgnore // Prevent infinite recursion
    private List<Order> orders=new ArrayList<>();

    public Users() {
    }

    public Long getUser_id() {
        return User_id;
    }

    public void setUser_id(Long user_id) {
        User_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
        for(Order order:orders)
            order.setUser(this);
    }

    public enum Role {
        CUSTOMER, RESTAURANT, ADMIN
    }
}
