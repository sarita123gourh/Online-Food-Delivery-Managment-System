package com.db.controllers;
import com.db.entities.MenuItem;
import com.db.services.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuItemController {

    @Autowired
    MenuItemService menuItemService;

//    @PostMapping
//    public ResponseEntity<MenuItem>createManuItem(@RequestBody MenuItem menuItem)
//    {
//       return ResponseEntity.ok(menuItemService.save(menuItem));
//    }
    // Add MenuItem By RestaurantId
       @PostMapping("/addItems")
       public ResponseEntity<MenuItem> createMenuItem(
        @RequestBody MenuItem menuItem,
        @RequestParam Long restaurantId) {
         MenuItem createdMenuItem = menuItemService.createMenuItem(menuItem, restaurantId);
        return new ResponseEntity<>(createdMenuItem, HttpStatus.CREATED);
      }

    @GetMapping("/getItems")
    public ResponseEntity<List<MenuItem>> getAllMenuItemsByRestaurant(@RequestParam Long restaurantId) {
        List<MenuItem> menuItems = menuItemService.getMenuItemsByRestaurant(restaurantId);
        return ResponseEntity.ok(menuItems);
    }

    //Restaurant can getMenuItemById
    @GetMapping("/getItemById")
    public ResponseEntity<MenuItem> getMenuItemById(
            @RequestParam Long menuItemId,
            @RequestParam Long restaurantId) {
        MenuItem menuItem = menuItemService.getMenuItemByIdAndRestaurant(menuItemId, restaurantId);
        return ResponseEntity.ok(menuItem);
    }
    //Restaurant can deleteItemById
    @DeleteMapping("/deleteItemById")
    public ResponseEntity<String> deleteMenuItemById(
            @RequestParam Long menuItemId,
            @RequestParam Long restaurantId) {
        menuItemService.deleteMenuItemByIdAndRestaurant(menuItemId, restaurantId);
        return ResponseEntity.ok("MenuItem with ID " + menuItemId + " has been successfully deleted.");
    }

   //update menuItem for specific restaurant
    @PutMapping("/updateItem")
    public ResponseEntity<MenuItem> updateMenuItemById(
            @RequestParam Long menuItemId,
            @RequestParam Long restaurantId,
            @RequestBody MenuItem updatedMenuItem) {
        MenuItem menuItem = menuItemService.updateMenuItemByIdAndRestaurant(menuItemId, restaurantId, updatedMenuItem);
        return ResponseEntity.ok(menuItem);
    }

}
