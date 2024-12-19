package com.db.services;

import com.db.entities.MenuItem;
import com.db.entities.Restaurant;
import com.db.repo.MenuItemRepository;
import com.db.repo.RestaurantRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MenuItemServiceImpl implements MenuItemService{



    @Autowired
    MenuItemRepository menuItemRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Override
    public MenuItem createMenuItem(MenuItem menuItem, Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + restaurantId));

        menuItem.setRestaurant(restaurant); // Establish relationship
        return menuItemRepository.save(menuItem);
    }
    @Override
    public List<MenuItem> getMenuItemsByRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found with ID: " + restaurantId));
        return menuItemRepository.findByRestaurant(restaurant);
    }

    @Override
    public MenuItem getMenuItemByIdAndRestaurant(Long menuItemId, Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found with ID: " + restaurantId));

        return menuItemRepository.findByMenuItemIdAndRestaurant(menuItemId, restaurant)
                .orElseThrow(() -> new RuntimeException(
                        "MenuItem not found with ID: " + menuItemId + " for Restaurant ID: " + restaurantId));
    }
    //Restaurant can getMenuItemById
    @Override
    public void deleteMenuItemByIdAndRestaurant(Long menuItemId, Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found with ID: " + restaurantId));

        MenuItem menuItem = menuItemRepository.findByMenuItemIdAndRestaurant(menuItemId, restaurant)
                .orElseThrow(() -> new RuntimeException(
                        "MenuItem not found with ID: " + menuItemId + " for Restaurant ID: " + restaurantId));

        menuItemRepository.delete(menuItem);
    }
    @Override
    public MenuItem updateMenuItemByIdAndRestaurant(Long menuItemId, Long restaurantId, MenuItem updatedMenuItem) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found with ID: " + restaurantId));

        MenuItem existingMenuItem = menuItemRepository.findByMenuItemIdAndRestaurant(menuItemId, restaurant)
                .orElseThrow(() -> new RuntimeException(
                        "MenuItem not found with ID: " + menuItemId + " for Restaurant ID: " + restaurantId));

        // Update fields
        existingMenuItem.setDishName(updatedMenuItem.getDishName());
        existingMenuItem.setDescription(updatedMenuItem.getDescription());
        existingMenuItem.setPrice(updatedMenuItem.getPrice());
        existingMenuItem.setAvailability(updatedMenuItem.getAvailability());

        // Save updated menu item
        return menuItemRepository.save(existingMenuItem);
    }

    @Override
    public MenuItem save(MenuItem menuItem) {
        return menuItemRepository.save(menuItem);
    }

    @Override
    public Iterable<MenuItem> findAll() {
        return menuItemRepository.findAll();
    }


}
