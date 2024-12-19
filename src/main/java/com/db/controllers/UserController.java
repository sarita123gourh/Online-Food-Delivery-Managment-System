package com.db.controllers;
import com.db.entities.Restaurant;
import com.db.entities.Users;
import com.db.services.MenuItemService;
import com.db.services.RestaurantService;
import com.db.services.UserService;
import com.db.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    RestaurantService restaurantService;

    @Autowired
    MenuItemService menuItemService;

    @GetMapping("/restaurants")
    public ResponseEntity<Iterable<Restaurant>> getAllRestaurants() {
        Iterable<Restaurant> restaurants = restaurantService.getAllRestaurants(); // Assuming this fetches restaurants
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/restaurants/{id}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable Long id) {
        Optional<Restaurant> restaurant = restaurantService.findById(id);
        return restaurant
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

//    @PostMapping("/login")
//    public ResponseEntity<?> save(@RequestBody Users user) {
//        System.out.println("User Controller call");
//        try {
//            Users savedUser = userService.saveUsers(user);
//            return ResponseEntity.ok(savedUser);
//        } catch (RuntimeException ex) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save user");
//        }
//    }

    @PostMapping("/Order")
    public ResponseEntity<Users> saveUserWithOrder(@RequestBody Users user)
    {
        return ResponseEntity.ok(userService.saveUsers(user));
    }

//    @GetMapping
//    public ResponseEntity<?> getAllUsers() {
//        try {
//            Iterable<Users> users = userService.getAllUsers();
//            return ResponseEntity.ok(users);
//        } catch (RuntimeException ex) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch users");
//        }
//    }
    @Autowired
    private UserServiceImpl service;


    @PostMapping("/register")
    public Users register(@RequestBody Users user) {
        System.out.println("/register (userController) call....");
        return service.register(user);

    }

    @PostMapping("/login")
    public String login(@RequestBody Users user) {

       return service.verify(user);
    }
    @GetMapping
    public ResponseEntity<List<Users>> getAllOrders() {
        List<Users> user = service.getAllUsers();
        return ResponseEntity.ok(user);
    }
}
