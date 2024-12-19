package com.db.controllers;

import com.db.entities.Order;
import com.db.entities.Restaurant;
import com.db.services.MenuItemService;
import com.db.services.OrderService;
import com.db.services.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/Restaurant")
public class RestaurantController {

    @Autowired
    RestaurantService restaurantService;

    @Autowired
    MenuItemService menuItemService;

    @Autowired
    OrderService orderService;

    //Create Restaurant By me
    @PostMapping
    public ResponseEntity<Restaurant> createRestaurant(@RequestBody Restaurant restaurant)
    {
        return ResponseEntity.ok(restaurantService.save(restaurant));
    }
    //create RestaurantWith MenuItems By me
    @PostMapping("/menu-Items")
    public ResponseEntity<Restaurant> createRestaurantwithMenuItems(@RequestBody Restaurant restaurant)
    {
        return ResponseEntity.ok(restaurantService.save(restaurant));
    }
   //AddMenu_ItemToRestaurant according to Restaurant Id
//    @PostMapping("/{restaurantId}/menu-items")
//    public ResponseEntity<MenuItem> addMenuItem(
//            @PathVariable Long restaurantId,
//            @RequestBody MenuItem menuItem) {
//        MenuItem savedMenuItem = menuItemService.addMenuItemToRestaurant(restaurantId, menuItem);
//        return ResponseEntity.status(HttpStatus.CREATED).body(savedMenuItem);
//    }
    //GetAllRestaurant
    @GetMapping
    public ResponseEntity<Iterable<Restaurant>>getAllRestaurant()
    {
        return ResponseEntity.ok((Iterable<Restaurant>) restaurantService.findAll());
    }

    //Get RestaurantById
    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable Long id) {
        Optional<Restaurant> restaurant = restaurantService.findById(id);
        return restaurant.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    //delete Restaurant By Id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) {
        restaurantService.deleteRestaurantById(id);
        return ResponseEntity.noContent().build();
    }
    //update Restaurant by Id
    @PutMapping("/{restaurantId}")
    public ResponseEntity<Restaurant> updateRestaurant(
            @PathVariable Long restaurantId,
            @RequestBody Restaurant updatedRestaurant) {
        Restaurant restaurant = restaurantService.updateRestaurant(restaurantId, updatedRestaurant);
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }
    //Restaurant can see there all Orders
    @GetMapping("/orders")
    public ResponseEntity<List<Order>> fetchAllOrders() {
        List<Order> orders = (List<Order>) orderService.findAll();
        System.out.println("Fetched Orders: " + orders);
        return ResponseEntity.ok(orders);
    }
    @GetMapping("/orders/{id}")
    public ResponseEntity<Order> fetchOrderById(@PathVariable Long id) {
        Order order = orderService.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + id));
        return ResponseEntity.ok(order);
    }
}
