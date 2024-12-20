package com.db.DTO;

import java.util.List;

public class OrderRequest {
    private String customer;
    private Long userId;  // ID of the user placing the order
    private List<Long> menuItemIds;  // List of menu item IDs

    public OrderRequest(Long userId, String johnDoe, Long[] menuItemIds) {
    }

    public OrderRequest() {

    }

    // Getters and Setters
    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Long> getMenuItemIds() {
        return menuItemIds;
    }

    public void setMenuItemIds(List<Long> menuItemIds) {
        this.menuItemIds = menuItemIds;
    }
}