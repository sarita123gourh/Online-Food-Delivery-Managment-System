package com.db.serviceImplTest;


import com.db.DTO.UserPrincipal;
import com.db.entities.Users;
import com.db.repo.UserRepository;
import com.db.services.MyUserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MyUserDetailsServiceImplTest {

    @InjectMocks
    private MyUserDetailsServiceImpl userDetailsService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsername_UserExists_ReturnsUserDetails() {
        // Arrange
        String username = "testuser";
        Users mockUser = new Users();
        mockUser.setUsername(username);
        mockUser.setPassword("password123");
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Assert
        assertNotNull(userDetails, "UserDetails should not be null");
        assertEquals(username, userDetails.getUsername(), "Username should match the expected value");
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void loadUserByUsername_UserDoesNotExist_ThrowsUsernameNotFoundException() {
        // Arrange
        String username = "nonexistentuser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(username),
                "Expected UsernameNotFoundException to be thrown"
        );
        assertEquals("user not found", exception.getMessage(), "Exception message should match");
        verify(userRepository, times(1)).findByUsername(username);
    }
}

