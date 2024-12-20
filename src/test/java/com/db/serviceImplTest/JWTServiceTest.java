package com.db.serviceImplTest;

import com.db.services.JWTService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JWTServiceTest {

    private JWTService jwtService;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtService = new JWTService();
    }

    @Test
    void testGenerateToken_shouldReturnValidToken() {
        // Arrange
        String username = "testUser";

        // Act
        String token = jwtService.generateToken(username);

        // Assert
        assertNotNull(token, "Token should not be null");
        assertTrue(token.length() > 0, "Token should not be empty");
    }

    @Test
    void testExtractUserName_shouldReturnUsername() {
        // Arrange
        String username = "testUser";
        String token = jwtService.generateToken(username);

        // Act
        String extractedUsername = jwtService.extractUserName(token);

        // Assert
        assertEquals(username, extractedUsername, "Extracted username should match the original username");
    }

    @Test
    void testValidateToken_shouldReturnTrueForValidToken() {
        // Arrange
        String username = "testUser";
        when(userDetails.getUsername()).thenReturn(username);
        String token = jwtService.generateToken(username);

        // Act
        boolean isValid = jwtService.validateToken(token, userDetails);

        // Assert
        assertTrue(isValid, "Token should be valid for the given user");
    }

    @Test
    void testValidateToken_shouldReturnFalseForInvalidUser() {
        // Arrange
        String username = "testUser";
        when(userDetails.getUsername()).thenReturn("anotherUser");
        String token = jwtService.generateToken(username);

        // Act
        boolean isValid = jwtService.validateToken(token, userDetails);

        // Assert
        assertFalse(isValid, "Token should be invalid for a different user");
    }

    @Test
    void testIsTokenExpired_shouldReturnFalseForValidToken() {
        // Arrange
        String validToken = jwtService.generateToken("testUser");

        // Act
        boolean isExpired = jwtService.isTokenExpired(validToken);

        // Assert
        assertFalse(isExpired, "Token should not be marked as expired");
    }

    @Test
    void testExtractExpiration_shouldReturnCorrectExpirationDate() {
        // Arrange
        String token = jwtService.generateToken("testUser");
        Date expectedExpiration = new Date(System.currentTimeMillis() + 60 * 60 * 1000);

        // Act
        Date expirationDate = jwtService.extractExpiration(token);

        // Assert
        assertNotNull(expirationDate, "Expiration date should not be null");
        assertEquals(expectedExpiration.getTime() / 1000, expirationDate.getTime() / 1000,
                "Extracted expiration date should match expected expiration date");
    }
}
