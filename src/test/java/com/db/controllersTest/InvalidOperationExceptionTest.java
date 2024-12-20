package com.db.controllersTest;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InvalidOperationExceptionTest {

    @Test
    void testExceptionMessage() {
        // Arrange
        String errorMessage = "Invalid operation occurred";

        // Act
        InvalidOperationException exception = new InvalidOperationException(errorMessage);

        // Assert
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void testExceptionThrown() {
        // Arrange
        String errorMessage = "This operation is not allowed";

        // Act & Assert
        Exception exception = assertThrows(InvalidOperationException.class, () -> {
            throw new InvalidOperationException(errorMessage);
        });

        // Verify the message
        assertEquals(errorMessage, exception.getMessage());
    }
}
