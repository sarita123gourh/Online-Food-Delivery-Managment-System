package com.db.controllersTest;

import com.db.DTO.OrderRequest;
import com.db.controllers.OrderController;
import com.db.entities.MenuItem;
import com.db.entities.Order;
import com.db.entities.Users;
import com.db.repo.MenuItemRepository;
import com.db.repo.UserRepository;
import com.db.services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderService orderService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MenuItemRepository menuItemRepository;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }


    @Test
    void testPlaceOrder_ShouldReturnBadRequest_WhenMenuItemsAreEmpty() {
        // Arrange
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setUserId(1L);
        orderRequest.setMenuItemIds(Arrays.asList(1L, 2L));
        orderRequest.setCustomer("Test Customer");

        Users user = new Users();
        user.setUser_id(1L);
        user.setUsername("testUser");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(menuItemRepository.findAllById(orderRequest.getMenuItemIds())).thenReturn(Arrays.asList());

        // Act
        ResponseEntity<Order> response = orderController.placeOrder(orderRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testPlaceOrder_ShouldReturnBadRequest_WhenUserNotFound() {
        // Arrange
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setUserId(1L);
        orderRequest.setMenuItemIds(Arrays.asList(1L, 2L));
        orderRequest.setCustomer("Test Customer");

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Order> response = orderController.placeOrder(orderRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }


    @Test
    void testPlaceOrder_ShouldReturnOkForSavedOrder() {
        // Arrange
        Order order = new Order();
        order.setOrderId(1L);
      //  order.setStatus("Pending");
        when(orderService.save(any(Order.class))).thenReturn(order);

        // Act
        ResponseEntity<Order> response = orderController.PlaceOrder(order);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }


}
