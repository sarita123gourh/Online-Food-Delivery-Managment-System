package com.db.controllersTest;

import com.db.controllers.OrderController;
import com.db.controllers.RestaurantController;
import com.db.entities.MenuItem;
import com.db.entities.Order;
import com.db.entities.Restaurant;
import com.db.services.OrderService;
import com.db.services.RestaurantService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class RestaurantControllerTest {

    @Mock
    private RestaurantService restaurantService; // Mock the service layer

    @InjectMocks
    private RestaurantController restaurantController; // Inject mocks into the controller

    @Mock
    private OrderService orderService; // Mock the service layer

    @InjectMocks
    private OrderController orderController;

    public RestaurantControllerTest() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    void testCreateRestaurant_Success() {
        // Arrange: Prepare the input and expected output
        Restaurant inputRestaurant = new Restaurant();
        inputRestaurant.setRestaurantName("Test Restaurant");
        inputRestaurant.setRestaurantAddress("Test Address");

        Restaurant savedRestaurant = new Restaurant();
        savedRestaurant.setRestaurantId(1L);
        savedRestaurant.setRestaurantName("Test Restaurant");
        savedRestaurant.setRestaurantAddress("Test Address");

        when(restaurantService.save(inputRestaurant)).thenReturn(savedRestaurant);

        // Act: Call the controller method
        ResponseEntity<Restaurant> response = restaurantController.createRestaurant(inputRestaurant);

        // Assert: Verify the response
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(savedRestaurant, response.getBody());
        verify(restaurantService, times(1)).save(inputRestaurant);
    }
    @Test
    void testCreateRestaurant_Failure() {
        // Arrange: Simulate a failure in the service layer
        Restaurant inputRestaurant = new Restaurant();
        inputRestaurant.setRestaurantName("Test Restaurant");
        inputRestaurant.setRestaurantAddress("Test Address");

        when(restaurantService.save(inputRestaurant)).thenThrow(new RuntimeException("Service error"));

        // Act and Assert: Call the controller and verify exception handling
        Exception exception = assertThrows(RuntimeException.class, () -> {
            restaurantController.createRestaurant(inputRestaurant);
        });

        assertEquals("Service error", exception.getMessage());
        verify(restaurantService, times(1)).save(inputRestaurant);
    }
    @Test
    void testCreateRestaurantWithMenuItems_Success() {
        // Arrange
        Restaurant inputRestaurant = new Restaurant();
        inputRestaurant.setRestaurantName("Restaurant With Menu");
        inputRestaurant.setRestaurantAddress("Some Address");
        inputRestaurant.setMenuItems(Arrays.asList(
                new MenuItem(1L, "Pizza", 10.99),
                new MenuItem(1L, "Burger", 5.99)
        ));

        Restaurant savedRestaurant = new Restaurant();
        savedRestaurant.setRestaurantId(1L);
        savedRestaurant.setRestaurantName("Restaurant With Menu");
        savedRestaurant.setRestaurantAddress("Some Address");
        savedRestaurant.setMenuItems(Arrays.asList(
                new MenuItem(1L, "Pizza", 10.99),
                new MenuItem(1L, "Burger", 5.99)
        ));

        when(restaurantService.save(inputRestaurant)).thenReturn(savedRestaurant);

        // Act
        ResponseEntity<Restaurant> response = restaurantController.createRestaurantwithMenuItems(inputRestaurant);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(savedRestaurant, response.getBody());
        verify(restaurantService, times(1)).save(inputRestaurant);
    }
    @Test
    void testCreateRestaurantWithMenuItems_Failure() {
        // Arrange: Simulate a failure in the service layer
        Restaurant inputRestaurant = new Restaurant();
        inputRestaurant.setRestaurantName("Restaurant With Menu");
        inputRestaurant.setRestaurantAddress("Some Address");
        inputRestaurant.setMenuItems(Arrays.asList(
                new MenuItem(1L, "Pizza", 10.99),
                new MenuItem(1L, "Burger", 5.99)
        ));

        when(restaurantService.save(inputRestaurant)).thenThrow(new RuntimeException("Service error"));

        // Act and Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            restaurantController.createRestaurantwithMenuItems(inputRestaurant);
        });

        assertEquals("Service error", exception.getMessage());
        verify(restaurantService, times(1)).save(inputRestaurant);
    }
    @Test
    void testGetRestaurantById_Success() {
        // Arrange
        Long restaurantId = 1L;
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId(restaurantId);
        restaurant.setRestaurantName("Test Restaurant");
        restaurant.setRestaurantAddress("Test Address");

        when(restaurantService.findById(restaurantId)).thenReturn(Optional.of(restaurant));

        // Act
        ResponseEntity<Restaurant> response = restaurantController.getRestaurantById(restaurantId);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(restaurant, response.getBody());
        verify(restaurantService, times(1)).findById(restaurantId);
    }
    @Test
    void testGetRestaurantById_Failure() {
        // Arrange
        Long restaurantId = 1L;

        when(restaurantService.findById(restaurantId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Restaurant> response = restaurantController.getRestaurantById(restaurantId);

        // Assert
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(restaurantService, times(1)).findById(restaurantId);
    }
    @Test
    void testDeleteRestaurant_Success() {
        // Arrange
        Long restaurantId = 1L;

        // Mock the service method to simulate successful deletion
        doNothing().when(restaurantService).deleteRestaurantById(restaurantId);

        // Act
        ResponseEntity<Void> response = restaurantController.deleteRestaurant(restaurantId);

        // Assert
        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(restaurantService, times(1)).deleteRestaurantById(restaurantId);
    }
    @Test
    void testDeleteRestaurant_Failure() {
        // Arrange
        Long restaurantId = 1L;

        // Mock the service method to simulate an exception
        doThrow(new RuntimeException("Restaurant not found")).when(restaurantService).deleteRestaurantById(restaurantId);

        // Act and Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            restaurantController.deleteRestaurant(restaurantId);
        });

        assertEquals("Restaurant not found", exception.getMessage());
        verify(restaurantService, times(1)).deleteRestaurantById(restaurantId);
    }
    @Test
    void testUpdateRestaurant_Success() {
        // Arrange
        Long restaurantId = 1L;

        Restaurant updatedRestaurant = new Restaurant();
        updatedRestaurant.setRestaurantName("Updated Restaurant");
        updatedRestaurant.setRestaurantAddress("Updated Address");

        Restaurant savedRestaurant = new Restaurant();
        savedRestaurant.setRestaurantId(restaurantId);
        savedRestaurant.setRestaurantName("Updated Restaurant");
        savedRestaurant.setRestaurantAddress("Updated Address");

        when(restaurantService.updateRestaurant(restaurantId, updatedRestaurant)).thenReturn(savedRestaurant);

        // Act
        ResponseEntity<Restaurant> response = restaurantController.updateRestaurant(restaurantId, updatedRestaurant);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(savedRestaurant, response.getBody());
        verify(restaurantService, times(1)).updateRestaurant(restaurantId, updatedRestaurant);
    }
    @Test
    void testUpdateRestaurant_Failure() {
        // Arrange
        Long restaurantId = 1L;

        Restaurant updatedRestaurant = new Restaurant();
        updatedRestaurant.setRestaurantName("Updated Restaurant");
        updatedRestaurant.setRestaurantAddress("Updated Address");

        when(restaurantService.updateRestaurant(restaurantId, updatedRestaurant))
                .thenThrow(new RuntimeException("Restaurant not found"));

        // Act and Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            restaurantController.updateRestaurant(restaurantId, updatedRestaurant);
        });

        assertEquals("Restaurant not found", exception.getMessage());
        verify(restaurantService, times(1)).updateRestaurant(restaurantId, updatedRestaurant);
    }
   // Success is missing
    @Test
    void testFetchAllOrders_Failure() {
        // Arrange
        when(orderService.findAll()).thenThrow(new RuntimeException("Error fetching orders"));

        // Act and Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            orderController.getAllOrders();
        });

        assertEquals("Error fetching orders", exception.getMessage());
        verify(orderService, times(1)).findAll();
    }

    @Test
    void testFetchOrderById_Success() {
        // Arrange
        Long orderId = 1L;
        Order order = new Order(orderId, "Order 1", "Pending");

        // Mock the service's findById method to return an Optional containing the order
        when(orderService.findById(orderId)).thenReturn(Optional.of(order));

        // Act
        ResponseEntity<Order> response = orderController.fetchOrderById(orderId);

        // Assert
        assertNotNull(response);  // Ensure the response is not null
        assertEquals(200, response.getStatusCodeValue());  // HTTP status code should be 200 OK
        assertEquals(order, response.getBody());  // Ensure the response body contains the order
        verify(orderService, times(1)).findById(orderId);  // Verify that findById was called once
    }

    @Test
    void testFetchOrderById_Failure() {
        // Arrange
        Long orderId = 1L;

        // Mock the service's findById method to return an empty Optional (simulating not found)
        when(orderService.findById(orderId)).thenReturn(Optional.empty());

        // Act and Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderController.fetchOrderById(orderId);
        });

        assertEquals("Order not found with ID: " + orderId, exception.getMessage());  // Verify the exception message
        verify(orderService, times(1)).findById(orderId);  // Verify that findById was called once
    }
}
