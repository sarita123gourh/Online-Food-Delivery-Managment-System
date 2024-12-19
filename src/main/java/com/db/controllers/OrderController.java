package com.db.controllers;

import com.db.DTO.OrderRequest;
import com.db.entities.MenuItem;
import com.db.entities.Order;
import com.db.entities.Users;
import com.db.repo.MenuItemRepository;
import com.db.repo.UserRepository;
import com.db.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Order")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    @Autowired
    UserRepository userRepository;

    @Autowired
    MenuItemRepository menuItemRepository;

    // Endpoint to place an order
    @PostMapping("/place")
    public ResponseEntity<Order> placeOrder(@RequestBody OrderRequest orderRequest) {
        try {
            // Fetch the user from the database
            Users user = userRepository.findById(orderRequest.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Fetch the menu items by IDs
            List<MenuItem> menuItems = (List<MenuItem>) menuItemRepository.findAllById(orderRequest.getMenuItemIds());
            if (menuItems.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            // Create a new order
            Order order = new Order(1L, "Order 1", "Pending");
            order.setCustomer(orderRequest.getCustomer());
            order.setUser(user);
            order.setMenuItems(menuItems);

            // Calculate the total amount
            double totalAmount = menuItems.stream().mapToDouble(MenuItem::getPrice).sum();
            order.setTotalAmount(totalAmount);

            // Save the order
            Order savedOrder = orderService.save(order);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


    // Endpoint to get all orders (for admin or customer to view their orders)
    @GetMapping
    public Iterable<Order> getAllOrders() {
        return orderService.findAll();
    }
    @PostMapping
    public ResponseEntity<Order> PlaceOrder(@RequestBody Order order)
    {
        return ResponseEntity.ok(orderService.save(order));
    }

    // Fetch order by ID
    @GetMapping("/{id}")
    public ResponseEntity<Order> fetchOrderById(@PathVariable Long id) {
        // Try to fetch the order by its ID
        Order order = orderService.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + id));

        // Return the found order wrapped in a ResponseEntity with HTTP 200 OK status
        return ResponseEntity.ok(order);
    }
}
