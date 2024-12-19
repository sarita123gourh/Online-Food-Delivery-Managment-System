package com.db.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuItemId;

    private String dishName;
    private String description;
    private Double price;
    private Boolean availability=true;

    @ManyToOne
   // @JoinColumn(name = "restaurant_id", nullable = false,referencedColumnName = "id")
    @JsonIgnore // Prevent infinite recursion
    private Restaurant restaurant;
    public MenuItem(long l, String pizza, double v){}

    public MenuItem() {

    }

    // Getters and Setters
    public Long getMenuItemId() {
        return menuItemId;
    }

    public MenuItem(Long menuItemId, String dishName, String description, Double price, Boolean availability, Restaurant restaurant) {
        this.menuItemId = menuItemId;
        this.dishName = dishName;
        this.description = description;
        this.price = price;
        this.availability = availability;
        this.restaurant = restaurant;
    }

    public void setMenuItemId(Long menuItemId) {
        this.menuItemId = menuItemId;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Boolean getAvailability() {
        return availability;
    }

    public void setAvailability(Boolean availability) {
        this.availability = availability;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
