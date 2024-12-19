package com.db.controllersTest;

import com.db.controllers.UserController;
import com.db.entities.Users;
import com.db.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    public UserControllerTest() {
        // Initialize mocks and setup MockMvc
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void testSaveUser_Success() throws Exception {
        // Mock the service layer
        Users mockUser = new Users();
        mockUser.setUser_id(1L);
        mockUser.setUsername("John Doe");

        Mockito.when(userService.saveUsers(any(Users.class))).thenReturn(mockUser);

        // Perform POST request
        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"John Doe\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"user_id\":1,\"username\":\"John Doe\"}"));
    }
    @Test
    void testSaveUser_Failure() throws Exception {
        // Mock the service layer to throw an exception
        Mockito.when(userService.saveUsers(any(Users.class)))
                .thenThrow(new RuntimeException("Service failed"));

        // Perform POST request
        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"John Doe\"}"))
                .andExpect(status().isInternalServerError()) // Check for 500 Internal Server Error
                .andExpect(content().string("Failed to save user")); // Verify error message
    }
    @Test
    void testGetAllUsers_Success() throws Exception {
        // Create mock user data
        Users user1 = new Users();
        user1.setUser_id(1L);
        user1.setUsername("John Doe");

        Users user2 = new Users();
        user2.setUser_id(2L);
        user2.setUsername("Jane Doe");

        // Mock the service layer to return a list of users
        Mockito.when(userService.findAll()).thenReturn(List.of(user1, user2));

        // Perform GET request
        mockMvc.perform(get("/user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Check for 200 OK status
                .andExpect(content().json("[{\"user_id\":1,\"username\":\"John Doe\"},{\"user_id\":2,\"username\":\"Jane Doe\"}]")); // Check the response body
    }

    @Test
    void testGetAllUsers_Failure() throws Exception {
        // Mock the service layer to throw an exception
        Mockito.when(userService.findAll()).thenThrow(new RuntimeException("Failed to fetch users"));

        // Perform GET request
        mockMvc.perform(get("/user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError()) // Check for 500 Internal Server Error status
                .andExpect(content().string("Failed to fetch users")); // Check for the error message in the response
    }

}
