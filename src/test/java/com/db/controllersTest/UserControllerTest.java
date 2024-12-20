package com.db.controllersTest;

import com.db.controllers.UserController;
import com.db.entities.Restaurant;
import com.db.entities.Users;
import com.db.services.MenuItemService;
import com.db.services.RestaurantService;
import com.db.services.UserService;
import com.db.services.UserServiceImpl;
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

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private RestaurantService restaurantService;

    @Mock
    private MenuItemService menuItemService;

    @Mock
    private UserServiceImpl service;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void testGetRestaurantById_ShouldReturnNotFound_WhenNotFound() throws Exception {
        // Arrange
        when(restaurantService.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/user/restaurants/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSaveUserWithOrder_ShouldReturnSavedUser() {
        // Arrange
        Users user = new Users();
        user.setUsername("testUser");
        when(userService.saveUsers(user)).thenReturn(user);

        // Act
        ResponseEntity<Users> response = userController.saveUserWithOrder(user);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("testUser", response.getBody().getUsername());
    }

    @Test
    void testRegister_ShouldReturnRegisteredUser() {
        // Arrange
        Users user = new Users();
        user.setUsername("newUser");
        when(service.register(user)).thenReturn(user);

        // Act
        Users registeredUser = userController.register(user);

        // Assert
        assertNotNull(registeredUser);
        assertEquals("newUser", registeredUser.getUsername());
    }

    @Test
    void testLogin_ShouldReturnToken_WhenUserIsValid() {
        // Arrange
        Users user = new Users();
        user.setUsername("testUser");
        when(service.verify(user)).thenReturn("validToken");

        // Act
        String token = userController.login(user);

        // Assert
        assertEquals("validToken", token);
    }

    @Test
    void testLogin_ShouldReturnError_WhenUserIsInvalid() {
        // Arrange
        Users user = new Users();
        user.setUsername("invalidUser");
        when(service.verify(user)).thenReturn("Invalid credentials");

        // Act
        String response = userController.login(user);

        // Assert
        assertEquals("Invalid credentials", response);
    }

    @Test
    void testGetAllOrders_ShouldReturnUsers() throws Exception {
        // Arrange
        Users user1 = new Users();
        user1.setUsername("testUser1");
        Users user2 = new Users();
        user2.setUsername("testUser2");

        List<Users> users = Arrays.asList(user1, user2);
        when(service.getAllUsers()).thenReturn(users);

        // Act & Assert
        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("testUser1"))
                .andExpect(jsonPath("$[1].username").value("testUser2"));
    }
}
