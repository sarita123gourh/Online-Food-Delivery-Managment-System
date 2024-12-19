package com.db.serviceImplTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.db.entities.MenuItem;
import com.db.entities.Restaurant;
import com.db.repo.MenuItemRepository;
import com.db.repo.RestaurantRepository;
import com.db.services.MenuItemServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

class MenuItemServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private MenuItemRepository menuItemRepository;

    @InjectMocks
    private MenuItemServiceImpl menuItemService; // Class under test

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    //test case for CreateMenuItem ***********************************************
    @Test
    void testCreateMenuItem_Success() {
        // Arrange
        Long restaurantId = 1L;
        Restaurant mockRestaurant = new Restaurant();
        mockRestaurant.setRestaurantId(restaurantId);
        mockRestaurant.setRestaurantName("Test Restaurant");

        MenuItem mockMenuItem = new MenuItem(1L, "Pizza", 10.99);
        mockMenuItem.setDishName("Test Menu Item");
        mockMenuItem.setPrice(10.99);

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(mockRestaurant));
        when(menuItemRepository.save(any(MenuItem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        MenuItem createdMenuItem = menuItemService.createMenuItem(mockMenuItem, restaurantId);

        // Assert
        assertNotNull(createdMenuItem);
        assertEquals("Test Menu Item", createdMenuItem.getDishName());
        assertEquals(10.99, createdMenuItem.getPrice());
        assertEquals(mockRestaurant, createdMenuItem.getRestaurant());

        // Verify interactions
        verify(restaurantRepository, times(1)).findById(restaurantId);
        verify(menuItemRepository, times(1)).save(mockMenuItem);
    }

    @Test
    void testCreateMenuItem_RestaurantNotFound() {
        // Arrange
        Long restaurantId = 1L;
        MenuItem mockMenuItem = new MenuItem(1L, "Pizza", 10.99);
        mockMenuItem.setDishName("Test Menu Item");
        mockMenuItem.setPrice(10.99);

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            menuItemService.createMenuItem(mockMenuItem, restaurantId);
        });

        assertEquals("Restaurant not found with id: " + restaurantId, exception.getMessage());

        // Verify interactions
        verify(restaurantRepository, times(1)).findById(restaurantId);
        verify(menuItemRepository, never()).save(any(MenuItem.class));
    }
    //test case for GetMenuItemsByRestaurant *************************************
    @Test
    void testGetMenuItemsByRestaurant_Success() {
        // Arrange
        Long restaurantId = 1L;
        Restaurant mockRestaurant = new Restaurant();
        mockRestaurant.setRestaurantId(restaurantId);

        List<MenuItem> mockMenuItems = new ArrayList<>();
        MenuItem menuItem1 = new MenuItem(1L, "Pizza", 10.99);
        menuItem1.setDishName("Pizza");
        MenuItem menuItem2 = new MenuItem(1L, "Pizza", 10.99);
        menuItem2.setDishName("Pasta");
        mockMenuItems.add(menuItem1);
        mockMenuItems.add(menuItem2);

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(mockRestaurant));
        when(menuItemRepository.findByRestaurant(mockRestaurant)).thenReturn(mockMenuItems);

        // Act
        List<MenuItem> result = menuItemService.getMenuItemsByRestaurant(restaurantId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Pizza", result.get(0).getDishName());
        assertEquals("Pasta", result.get(1).getDishName());

        // Verify interactions
        verify(restaurantRepository, times(1)).findById(restaurantId);
        verify(menuItemRepository, times(1)).findByRestaurant(mockRestaurant);
    }

    @Test
    void testGetMenuItemsByRestaurant_Failure() {
        // Arrange
        Long restaurantId = 1L;

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            menuItemService.getMenuItemsByRestaurant(restaurantId);
        });

        assertEquals("Restaurant not found with ID: " + restaurantId, exception.getMessage());

        // Verify interactions
        verify(restaurantRepository, times(1)).findById(restaurantId);
        verify(menuItemRepository, never()).findByRestaurant(any());
    }

    //test case for GetMenuItemByIdAndRestaurant *************************************
    @Test
    void testGetMenuItemByIdAndRestaurant_Success() {
        // Arrange
        Long restaurantId = 1L;
        Long menuItemId = 10L;

        Restaurant mockRestaurant = new Restaurant();
        mockRestaurant.setRestaurantId(restaurantId);

        MenuItem mockMenuItem = new MenuItem(1L, "Pizza", 10.99);
        mockMenuItem.setMenuItemId(menuItemId);
        mockMenuItem.setDishName("Burger");
        mockMenuItem.setRestaurant(mockRestaurant);

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(mockRestaurant));
        when(menuItemRepository.findByMenuItemIdAndRestaurant(menuItemId, mockRestaurant))
                .thenReturn(Optional.of(mockMenuItem));

        // Act
        MenuItem result = menuItemService.getMenuItemByIdAndRestaurant(menuItemId, restaurantId);

        // Assert
        assertNotNull(result);
        assertEquals(menuItemId, result.getMenuItemId());
        assertEquals("Burger", result.getDishName());
        assertEquals(mockRestaurant, result.getRestaurant());

        // Verify interactions
        verify(restaurantRepository, times(1)).findById(restaurantId);
        verify(menuItemRepository, times(1)).findByMenuItemIdAndRestaurant(menuItemId, mockRestaurant);
    }

    @Test
    void testGetMenuItemByIdAndRestaurant_RestaurantNotFound() {
        // Arrange
        Long restaurantId = 1L;
        Long menuItemId = 10L;

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            menuItemService.getMenuItemByIdAndRestaurant(menuItemId, restaurantId);
        });

        assertEquals("Restaurant not found with ID: " + restaurantId, exception.getMessage());

        // Verify interactions
        verify(restaurantRepository, times(1)).findById(restaurantId);
        verify(menuItemRepository, never()).findByMenuItemIdAndRestaurant(anyLong(), any());
    }

    @Test
    void testGetMenuItemByIdAndRestaurant_MenuItemNotFound() {
        // Arrange
        Long restaurantId = 1L;
        Long menuItemId = 10L;

        Restaurant mockRestaurant = new Restaurant();
        mockRestaurant.setRestaurantId(restaurantId);

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(mockRestaurant));
        when(menuItemRepository.findByMenuItemIdAndRestaurant(menuItemId, mockRestaurant))
                .thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            menuItemService.getMenuItemByIdAndRestaurant(menuItemId, restaurantId);
        });

        assertEquals("MenuItem not found with ID: " + menuItemId + " for Restaurant ID: " + restaurantId, exception.getMessage());

        // Verify interactions
        verify(restaurantRepository, times(1)).findById(restaurantId);
        verify(menuItemRepository, times(1)).findByMenuItemIdAndRestaurant(menuItemId, mockRestaurant);
    }

    //test case for DeleteMenuItemsByIdandRestaurant *************************************

    @Test
    void testDeleteMenuItemByIdAndRestaurant_Success() {
        // Arrange
        Long restaurantId = 1L;
        Long menuItemId = 10L;

        Restaurant mockRestaurant = new Restaurant();
        mockRestaurant.setRestaurantId(restaurantId);

        MenuItem mockMenuItem = new MenuItem(1L, "Pizza", 10.99);
        mockMenuItem.setMenuItemId(menuItemId);
        mockMenuItem.setRestaurant(mockRestaurant);

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(mockRestaurant));
        when(menuItemRepository.findByMenuItemIdAndRestaurant(menuItemId, mockRestaurant))
                .thenReturn(Optional.of(mockMenuItem));

        // Act
        menuItemService.deleteMenuItemByIdAndRestaurant(menuItemId, restaurantId);

        // Assert
        // Verify delete is called
        verify(menuItemRepository, times(1)).delete(mockMenuItem);

        // Verify interactions
        verify(restaurantRepository, times(1)).findById(restaurantId);
        verify(menuItemRepository, times(1)).findByMenuItemIdAndRestaurant(menuItemId, mockRestaurant);
    }

//DeleteMenuItemByIdAndRestaurant **********************************************

    @Test
    void testDeleteMenuItemByIdAndRestaurant_RestaurantNotFound() {
        // Arrange
        Long restaurantId = 1L;
        Long menuItemId = 10L;

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            menuItemService.deleteMenuItemByIdAndRestaurant(menuItemId, restaurantId);
        });

        assertEquals("Restaurant not found with ID: " + restaurantId, exception.getMessage());

        // Verify interactions
        verify(restaurantRepository, times(1)).findById(restaurantId);
        verify(menuItemRepository, never()).findByMenuItemIdAndRestaurant(anyLong(), any());
        verify(menuItemRepository, never()).delete(any());
    }

    @Test
    void testDeleteMenuItemByIdAndRestaurant_MenuItemNotFound() {
        // Arrange
        Long restaurantId = 1L;
        Long menuItemId = 10L;

        Restaurant mockRestaurant = new Restaurant();
        mockRestaurant.setRestaurantId(restaurantId);

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(mockRestaurant));
        when(menuItemRepository.findByMenuItemIdAndRestaurant(menuItemId, mockRestaurant))
                .thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            menuItemService.deleteMenuItemByIdAndRestaurant(menuItemId, restaurantId);
        });

        assertEquals("MenuItem not found with ID: " + menuItemId + " for Restaurant ID: " + restaurantId, exception.getMessage());

        // Verify interactions
        verify(restaurantRepository, times(1)).findById(restaurantId);
        verify(menuItemRepository, times(1)).findByMenuItemIdAndRestaurant(menuItemId, mockRestaurant);
        verify(menuItemRepository, never()).delete(any());
    }
    //UpdateMenuItemByIdAndRestaurant ***********************************

    @Test
    void testUpdateMenuItemByIdAndRestaurant_Success() {
        // Arrange
        Long restaurantId = 1L;
        Long menuItemId = 10L;

        Restaurant mockRestaurant = new Restaurant();
        mockRestaurant.setRestaurantId(restaurantId);

        MenuItem existingMenuItem = new MenuItem(1L, "Pizza", 10.99);
        existingMenuItem.setMenuItemId(menuItemId);
        existingMenuItem.setRestaurant(mockRestaurant);
        existingMenuItem.setDishName("Old Dish");
        existingMenuItem.setDescription("Old Description");
        existingMenuItem.setPrice(10.99);
        existingMenuItem.setAvailability(true);

        MenuItem updatedMenuItem = new MenuItem(1L, "Pizza", 10.99);
        updatedMenuItem.setDishName("Updated Dish");
        updatedMenuItem.setDescription("Updated Description");
        updatedMenuItem.setPrice(15.99);
        updatedMenuItem.setAvailability(false);

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(mockRestaurant));
        when(menuItemRepository.findByMenuItemIdAndRestaurant(menuItemId, mockRestaurant))
                .thenReturn(Optional.of(existingMenuItem));
        when(menuItemRepository.save(existingMenuItem)).thenReturn(existingMenuItem);

        // Act
        MenuItem result = menuItemService.updateMenuItemByIdAndRestaurant(menuItemId, restaurantId, updatedMenuItem);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Dish", result.getDishName());
        assertEquals("Updated Description", result.getDescription());
        assertEquals(15.99, result.getPrice());
        assertFalse(result.getAvailability());

        // Verify interactions
        verify(restaurantRepository, times(1)).findById(restaurantId);
        verify(menuItemRepository, times(1)).findByMenuItemIdAndRestaurant(menuItemId, mockRestaurant);
        verify(menuItemRepository, times(1)).save(existingMenuItem);
    }

    @Test
    void testUpdateMenuItemByIdAndRestaurant_RestaurantNotFound() {
        // Arrange
        Long restaurantId = 1L;
        Long menuItemId = 10L;

        MenuItem updatedMenuItem = new MenuItem(1L, "Pizza", 10.99);
        updatedMenuItem.setDishName("Updated Dish");

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            menuItemService.updateMenuItemByIdAndRestaurant(menuItemId, restaurantId, updatedMenuItem);
        });

        assertEquals("Restaurant not found with ID: " + restaurantId, exception.getMessage());

        // Verify interactions
        verify(restaurantRepository, times(1)).findById(restaurantId);
        verify(menuItemRepository, never()).findByMenuItemIdAndRestaurant(anyLong(), any());
        verify(menuItemRepository, never()).save(any());
    }

    @Test
    void testUpdateMenuItemByIdAndRestaurant_MenuItemNotFound() {
        // Arrange
        Long restaurantId = 1L;
        Long menuItemId = 10L;

        Restaurant mockRestaurant = new Restaurant();
        mockRestaurant.setRestaurantId(restaurantId);

        MenuItem updatedMenuItem = new MenuItem(1L, "Pizza", 10.99);
        updatedMenuItem.setDishName("Updated Dish");

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(mockRestaurant));
        when(menuItemRepository.findByMenuItemIdAndRestaurant(menuItemId, mockRestaurant)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            menuItemService.updateMenuItemByIdAndRestaurant(menuItemId, restaurantId, updatedMenuItem);
        });

        assertEquals("MenuItem not found with ID: " + menuItemId + " for Restaurant ID: " + restaurantId, exception.getMessage());

        // Verify interactions
        verify(restaurantRepository, times(1)).findById(restaurantId);
        verify(menuItemRepository, times(1)).findByMenuItemIdAndRestaurant(menuItemId, mockRestaurant);
        verify(menuItemRepository, never()).save(any());
    }
    //save and FindAll ***********************************************************

    @Test
    void testSaveMenuItem_Success() {
        // Arrange
        MenuItem menuItem = new MenuItem(1L, "Pizza", 10.99);
        menuItem.setDishName("Burger");
        menuItem.setPrice(12.99);

        when(menuItemRepository.save(menuItem)).thenReturn(menuItem);

        // Act
        MenuItem result = menuItemService.save(menuItem);

        // Assert
        assertNotNull(result);
        assertEquals("Burger", result.getDishName());
        assertEquals(12.99, result.getPrice());

        // Verify interaction
        verify(menuItemRepository, times(1)).save(menuItem);
    }

    @Test
    void testSaveMenuItem_Failure() {
        // Arrange
        MenuItem menuItem = null;

        when(menuItemRepository.save(menuItem)).thenThrow(new IllegalArgumentException("MenuItem cannot be null"));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            menuItemService.save(menuItem);
        });

        assertEquals("MenuItem cannot be null", exception.getMessage());

        // Verify interaction
        verify(menuItemRepository, times(1)).save(menuItem);
    }

    @Test
    void testFindAllMenuItems_Success() {
        // Arrange
        MenuItem menuItem1 = new MenuItem(1L, "Pizza", 10.99);
        menuItem1.setDishName("Pizza");
        menuItem1.setPrice(15.99);

        MenuItem menuItem2 = new MenuItem(1L, "Pizza", 10.99);
        menuItem2.setDishName("Pasta");
        menuItem2.setPrice(11.99);

        when(menuItemRepository.findAll()).thenReturn(Arrays.asList(menuItem1, menuItem2));

        // Act
        Iterable<MenuItem> result = menuItemService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, ((Collection<?>) result).size());

        // Verify interaction
        verify(menuItemRepository, times(1)).findAll();
    }

    @Test
    void testFindAllMenuItems_EmptyList() {
        // Arrange
        when(menuItemRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        Iterable<MenuItem> result = menuItemService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(0, ((Collection<?>) result).size());

        // Verify interaction
        verify(menuItemRepository, times(1)).findAll();
    }
}

