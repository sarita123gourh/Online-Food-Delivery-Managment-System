package com.db.controllersTest;

import com.db.controllers.MenuItemController;
import com.db.entities.MenuItem;
import com.db.exception.ResourceNotFoundException;
import com.db.services.MenuItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MenuItemControllerTest {

    @InjectMocks
    private MenuItemController menuItemController;

    @Mock
    private MenuItemService menuItemService;

    private MenuItem menuItem;


    private final Long menuItemId = 1L;
    private final Long restaurantId = 1L;
    private MenuItem updatedMenuItem;

    @BeforeEach
    void setUp() {
        updatedMenuItem = new MenuItem(1L, "Pizza", 10.99);
        updatedMenuItem.setMenuItemId(menuItemId);
        updatedMenuItem.setDishName("Updated Dish");
        updatedMenuItem.setPrice(15.99);
    }


    @Test
    void testCreateMenuItem() {
        Long restaurantId = 1L;

        // Mocking the service
        when(menuItemService.createMenuItem(menuItem, restaurantId)).thenReturn(menuItem);

        // Perform the test
        ResponseEntity<MenuItem> response = menuItemController.createMenuItem(menuItem, restaurantId);

        // Verify the result
        assertEquals(201, response.getStatusCodeValue()); // HTTP Status Created
        assertEquals(menuItem, response.getBody()); // Correct MenuItem returned

        // Verify interaction with the service
        verify(menuItemService, times(1)).createMenuItem(menuItem, restaurantId);
    }


    @Test
    void testGetAllMenuItemsByRestaurant_NoItemsFound() {
        Long restaurantId = 1L;

        // Mocking the service for no items found
        when(menuItemService.getMenuItemsByRestaurant(restaurantId)).thenReturn(Collections.emptyList());

        // Perform the test
        ResponseEntity<List<MenuItem>> response = menuItemController.getAllMenuItemsByRestaurant(restaurantId);

        // Verify the result
        assertEquals(200, response.getStatusCodeValue()); // HTTP Status OK
        assertEquals(Collections.emptyList(), response.getBody()); // Empty list returned

        // Verify interaction with the service
        verify(menuItemService, times(1)).getMenuItemsByRestaurant(restaurantId);
    }

    @Test
    void testGetAllMenuItemsByRestaurant_ServiceThrowsException() {
        Long restaurantId = 1L;

        // Mocking the service to throw an exception
        when(menuItemService.getMenuItemsByRestaurant(restaurantId))
                .thenThrow(new RuntimeException("Restaurant not found"));

        // Perform the test
        try {
            menuItemController.getAllMenuItemsByRestaurant(restaurantId);
        } catch (Exception e) {
            // Verify the exception message
            assertEquals("Restaurant not found", e.getMessage());
        }

        // Verify interaction with the service
        verify(menuItemService, times(1)).getMenuItemsByRestaurant(restaurantId);
    }
    @Test
    void testDeleteMenuItemById_Success() {
        Long menuItemId = 1L;
        Long restaurantId = 101L;

        // Mocking the service behavior for a successful deletion
        doNothing().when(menuItemService).deleteMenuItemByIdAndRestaurant(menuItemId, restaurantId);

        // Perform the test
        ResponseEntity<String> response = menuItemController.deleteMenuItemById(menuItemId, restaurantId);

        // Verify the result
        assertEquals(200, response.getStatusCodeValue()); // HTTP Status OK
        assertEquals("MenuItem with ID 1 has been successfully deleted.", response.getBody());

        // Verify interaction with the service
        verify(menuItemService, times(1)).deleteMenuItemByIdAndRestaurant(menuItemId, restaurantId);
    }

    @Test
    void testDeleteMenuItemById_Failure() {
        Long menuItemId = 1L;
        Long restaurantId = 101L;

        // Mocking the service to throw an exception
        doThrow(new RuntimeException("MenuItem not found or does not belong to the specified restaurant"))
                .when(menuItemService).deleteMenuItemByIdAndRestaurant(menuItemId, restaurantId);

        // Perform the test
        try {
            menuItemController.deleteMenuItemById(menuItemId, restaurantId);
        } catch (Exception ex) {
            // Verify the exception message
            assertEquals("MenuItem not found or does not belong to the specified restaurant", ex.getMessage());
        }

        // Verify interaction with the service
        verify(menuItemService, times(1)).deleteMenuItemByIdAndRestaurant(menuItemId, restaurantId);
    }
    @Test
    void updateMenuItemById_Success() {
        // Mock the service layer
        when(menuItemService.updateMenuItemByIdAndRestaurant(menuItemId, restaurantId, updatedMenuItem))
                .thenReturn(updatedMenuItem);

        // Call the method
        ResponseEntity<MenuItem> response = menuItemController.updateMenuItemById(menuItemId, restaurantId, updatedMenuItem);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Updated Dish", response.getBody().getDishName());
        assertEquals(15.99, response.getBody().getPrice());
    }

    @Test
    void updateMenuItemById_MenuItemNotFound() {
        // Mock the service to throw an exception
        when(menuItemService.updateMenuItemByIdAndRestaurant(menuItemId, restaurantId, updatedMenuItem))
                .thenThrow(new ResourceNotFoundException("MenuItem not found"));

        // Call the method and assert exception
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> menuItemController.updateMenuItemById(menuItemId, restaurantId, updatedMenuItem)
        );

        assertEquals("MenuItem not found", exception.getMessage());
    }

    @Test
    void updateMenuItemById_RestaurantMismatch() {
        // Mock the service to throw an exception
        when(menuItemService.updateMenuItemByIdAndRestaurant(menuItemId, restaurantId, updatedMenuItem))
                .thenThrow(new InvalidOperationException("MenuItem does not belong to this restaurant"));

        // Call the method and assert exception
        InvalidOperationException exception = assertThrows(
                InvalidOperationException.class,
                () -> menuItemController.updateMenuItemById(menuItemId, restaurantId, updatedMenuItem)
        );

        assertEquals("MenuItem does not belong to this restaurant", exception.getMessage());
    }

    @Test
    void updateMenuItemById_InvalidRestaurantId() {
        // Mock the service to throw an exception
        when(menuItemService.updateMenuItemByIdAndRestaurant(menuItemId, -1L, updatedMenuItem))
                .thenThrow(new IllegalArgumentException("Invalid restaurant ID"));

        // Call the method and assert exception
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> menuItemController.updateMenuItemById(menuItemId, -1L, updatedMenuItem)
        );

        assertEquals("Invalid restaurant ID", exception.getMessage());
    }

}
