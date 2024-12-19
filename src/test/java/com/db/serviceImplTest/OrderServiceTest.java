package com.db.serviceImplTest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.db.entities.Order;
import com.db.repo.OrderRepository;
import com.db.services.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService; // Class under test

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    void testSaveOrder_Success() {
        // Arrange
        Order order = new Order(1L, "Order 1", "Pending");
        order.setOrderId(1L);
        //order.setStatus("Pending");

        when(orderRepository.save(order)).thenReturn(order);

        // Act
        Order result = orderService.save(order);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getOrderId());
      //  assertEquals("Pending", result.getStatus());

        // Verify interaction
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testSaveOrder_Failure() {
        // Arrange
        Order order = null;

        when(orderRepository.save(order)).thenThrow(new IllegalArgumentException("Order cannot be null"));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.save(order);
        });

        assertEquals("Order cannot be null", exception.getMessage());

        // Verify interaction
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testFindAllOrders_Success() {
        // Arrange
        Order order1 = new Order(1L, "Order 1", "Pending");
        order1.setOrderId(1L);
        //order1.setStatus("Pending");

        Order order2 = new Order(1L, "Order 1", "Pending");
        order2.setOrderId(2L);
       // order2.setStatus("Completed");

        when(orderRepository.findAll()).thenReturn(Arrays.asList(order1, order2));

        // Act
        Iterable<Order> result = orderService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, ((Collection<?>) result).size());

        // Verify interaction
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testFindAllOrders_EmptyList() {
        // Arrange
        when(orderRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        Iterable<Order> result = orderService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(0, ((Collection<?>) result).size());

        // Verify interaction
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testFindById_Success() {
        // Arrange
        Long orderId = 1L;
        Order order = new Order(1L, "Order 1", "Pending");
        order.setOrderId(orderId);
      //  order.setStatus("Pending");

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // Act
        Optional<Order> result = orderService.findById(orderId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(orderId, result.get().getOrderId());
        //assertEquals("Pending", result.get().getStatus());

        // Verify interaction
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void testFindById_NotFound() {
        // Arrange
        Long orderId = 1L;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // Act
        Optional<Order> result = orderService.findById(orderId);

        // Assert
        assertFalse(result.isPresent());

        // Verify interaction
        verify(orderRepository, times(1)).findById(orderId);
    }
}

