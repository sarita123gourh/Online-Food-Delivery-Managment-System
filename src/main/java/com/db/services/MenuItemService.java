package com.db.services;

import com.db.entities.MenuItem;

import java.util.List;
import java.util.Optional;

public interface MenuItemService {

    MenuItem save(MenuItem menuItem);

    Iterable<MenuItem> findAll();
    MenuItem createMenuItem(MenuItem menuItem, Long restaurantId);
    public List<MenuItem> getMenuItemsByRestaurant(Long restaurantId);
    public MenuItem getMenuItemByIdAndRestaurant(Long menuItemId, Long restaurantId);
    //Restaurant can getMenuItemById

    public void deleteMenuItemByIdAndRestaurant(Long menuItemId, Long restaurantId);

    //Restaurant can UpdateMenuItemById
    public MenuItem updateMenuItemByIdAndRestaurant(Long menuItemId, Long restaurantId, MenuItem updatedMenuItem);

    //for Order user can find Items According to the restaurant

}
