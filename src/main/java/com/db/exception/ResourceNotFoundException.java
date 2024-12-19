package com.db.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Custom exception to return 404 Not Found response
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    // Constructor that takes a message
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

