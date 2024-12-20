package com.db.serviceImplTest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.db.entities.Users;
import com.db.repo.UserRepository;
import com.db.services.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService; // The service class being tested

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    // Test for saveUsers success


    // Test for findAll success
    @Test
    void testFindAll_Success() {
        // Arrange
        Users user1 = new Users();
        user1.setUser_id(1L);
        user1.setUsername("user1");
        user1.setPassword("password1");
        user1.setRole(Users.Role.CUSTOMER);

        Users user2 = new Users();
        user2.setUser_id(2L);
        user2.setUsername("user2");
        user2.setPassword("password2");
        user2.setRole(Users.Role.ADMIN);

        List<Users> users = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(users);

        // Act
        Iterable<Users> result = userService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, ((List<Users>) result).size());

        // Verify interactions
        verify(userRepository, times(1)).findAll();
    }

    // Test for findAll empty result
    @Test
    void testFindAll_EmptyResult() {
        // Arrange
        when(userRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        Iterable<Users> result = userService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(0, ((List<Users>) result).size());

        // Verify interactions
        verify(userRepository, times(1)).findAll();
    }
}

