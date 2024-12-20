package com.db.serviceImplTest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.db.entities.Restaurant;
import com.db.repo.RestaurantRepository;
import com.db.services.RestaurantServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private RestaurantServiceImpl restaurantService; // Class under test

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    // Test for save() method

    @Test
    void testSaveRestaurant_Success() {
        // Arrange
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId(1L);
        restaurant.setRestaurantName("Test Restaurant");

        when(restaurantRepository.save(restaurant)).thenReturn(restaurant);

        // Act
        Restaurant result = restaurantService.save(restaurant);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getRestaurantId());
        assertEquals("Test Restaurant", result.getRestaurantName());

        // Verify interaction
        verify(restaurantRepository, times(1)).save(restaurant);
    }

    // Test for getAllRestaurants() method
    @Test
    void testGetAllRestaurants_Success() {
        // Arrange
        Restaurant restaurant1 = new Restaurant();
        restaurant1.setRestaurantId(1L);
        restaurant1.setRestaurantName("Restaurant 1");

        Restaurant restaurant2 = new Restaurant();
        restaurant2.setRestaurantId(2L);
        restaurant2.setRestaurantName("Restaurant 2");

        when(restaurantRepository.findAll()).thenReturn(Arrays.asList(restaurant1, restaurant2));

        // Act
        Iterable<Restaurant> result = restaurantService.getAllRestaurants();

        // Assert
        assertNotNull(result);
        assertEquals(2, ((Collection<?>) result).size());

        // Verify interaction
        verify(restaurantRepository, times(1)).findAll();
    }

    @Test
    void testGetAllRestaurants_EmptyList() {
        // Arrange
        when(restaurantRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        Iterable<Restaurant> result = restaurantService.getAllRestaurants();

        // Assert
        assertNotNull(result);
        assertEquals(0, ((Collection<?>) result).size());

        // Verify interaction
        verify(restaurantRepository, times(1)).findAll();
    }

    // Test for findById() method
    @Test
    void testFindById_Success() {
        // Arrange
        Long restaurantId = 1L;
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId(restaurantId);
        restaurant.setRestaurantName("Test Restaurant");

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));

        // Act
        Optional<Restaurant> result = restaurantService.findById(restaurantId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Test Restaurant", result.get().getRestaurantName());

        // Verify interaction
        verify(restaurantRepository, times(1)).findById(restaurantId);
    }

    @Test
    void testFindById_NotFound() {
        // Arrange
        Long restaurantId = 1L;

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        // Act
        Optional<Restaurant> result = restaurantService.findById(restaurantId);

        // Assert
        assertFalse(result.isPresent());

        // Verify interaction
        verify(restaurantRepository, times(1)).findById(restaurantId);
    }

    // Test for deleteRestaurantById() method
    @Test
    void testDeleteRestaurantById_Success() {
        // Arrange
        Long restaurantId = 1L;

        // No return value for deleteById; no stubbing necessary
        doNothing().when(restaurantRepository).deleteById(restaurantId);

        // Act
        restaurantService.deleteRestaurantById(restaurantId);

        // Verify interaction
        verify(restaurantRepository, times(1)).deleteById(restaurantId);
    }
    // Test for success case Update Restaurant
    @Test
    void testUpdateRestaurant_Success() {
        // Arrange
        Long restaurantId = 1L;

        Restaurant existingRestaurant = new Restaurant();
        existingRestaurant.setRestaurantId(restaurantId);
        existingRestaurant.setRestaurantName("Old Name");
        existingRestaurant.setRestaurantAddress("Old Address");
        existingRestaurant.setRestaurantPhone("1234567890");
        existingRestaurant.setApproved(false);

        Restaurant updatedRestaurant = new Restaurant();
        updatedRestaurant.setRestaurantName("New Name");
        updatedRestaurant.setRestaurantAddress("New Address");
        updatedRestaurant.setRestaurantPhone("0987654321");
        updatedRestaurant.setApproved(true);

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(existingRestaurant));
        when(restaurantRepository.save(existingRestaurant)).thenReturn(existingRestaurant);

        // Act
        Restaurant result = restaurantService.updateRestaurant(restaurantId, updatedRestaurant);

        // Assert
        assertNotNull(result);
        assertEquals("New Name", result.getRestaurantName());
        assertEquals("New Address", result.getRestaurantAddress());
        assertEquals("0987654321", result.getRestaurantPhone());
        assertTrue(result.isApproved());

        // Verify interaction
        verify(restaurantRepository, times(1)).findById(restaurantId);
        verify(restaurantRepository, times(1)).save(existingRestaurant);
    }

    // Test for failure case (restaurant not found)
    @Test
    void testUpdateRestaurant_Failure_NotFound() {
        // Arrange
        Long restaurantId = 1L;

        Restaurant updatedRestaurant = new Restaurant();
        updatedRestaurant.setRestaurantName("New Name");
        updatedRestaurant.setRestaurantAddress("New Address");
        updatedRestaurant.setRestaurantPhone("0987654321");
        updatedRestaurant.setApproved(true);

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            restaurantService.updateRestaurant(restaurantId, updatedRestaurant);
        });

        assertEquals("Restaurant not found with id: " + restaurantId, exception.getMessage());

        // Verify interaction
        verify(restaurantRepository, times(1)).findById(restaurantId);
        verify(restaurantRepository, never()).save(any(Restaurant.class));
    }
}

