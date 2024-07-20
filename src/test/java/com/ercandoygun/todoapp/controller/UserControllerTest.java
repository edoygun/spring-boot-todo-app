package com.ercandoygun.todoapp.controller;

import com.ercandoygun.todoapp.auth.util.JwtUtil;
import com.ercandoygun.todoapp.model.User;
import com.ercandoygun.todoapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



import com.ercandoygun.todoapp.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@WebMvcTest(value = UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    public void testCreateUser() throws Exception {
        User user = new User();
        user.setName("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password");

        when(userService.registerUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("testuser")))
                .andExpect(jsonPath("$.email", is("test@example.com")))
                .andExpect(jsonPath("$.password", is("password")));
    }

    @Test
    public void getAllUsers_ReturnsUsersList_WhenCalled() throws Exception {
        User user1 = new User();
        user1.setName("testuser1");
        User user2 = new User();
        user2.setName("testuser2");

        List<User> users = Arrays.asList(user1, user2);
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("testuser1")))
                .andExpect(jsonPath("$[1].name", is("testuser2")));
    }

    @Test
    public void updateUser_ReturnsUpdatedUser_WhenCalledWithValidUser() throws Exception {
        User user = new User("1", "UpdatedUser", "updated@example.com", "newpassword", new Date(), new Date());  // Assuming User has a constructor
        when(userService.updateUser(any(User.class))).thenReturn(user);

        mockMvc.perform(put("/users/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("UpdatedUser")));
    }

    @Test
    public void updatePassword_ReturnsUpdatedUser_WhenCalledWithValidData() throws Exception {
        User updatedUser = new User("1", "User1", "user1@example.com", "newpassword", new Date(), new Date());
        when(userService.updatePassword(any(String.class), any(String.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/users/update-password")
                        .param("userId", "1")
                        .param("newPassword", "newpassword")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("User1")))
                .andExpect(jsonPath("$.password", is("newpassword")));  // Ensure your User class does not expose passwords!
    }

    @Test
    public void registerUser_WithInvalidData_ReturnsBadRequest() throws Exception {
        User invalidUser = new User(null, "abcd", "bademail", "password123", new Date(), new Date());  // Invalid username and email

        MvcResult result = mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest()).andReturn();

        assertEquals(result.getResponse().getContentAsString(), "[\"Email should be valid\"]");
    }

    @Test
    public void updatePassword_WithNonexistentUserId_ReturnsNotFound() throws Exception {
        when(userService.updatePassword("999", "newPassword123"))
                .thenThrow(new UsernameNotFoundException("User not found"));

        MvcResult result = mockMvc.perform(put("/users/update-password")
                        .param("userId", "999")
                        .param("newPassword", "newPassword123"))
                .andExpect(status().isNotFound()).andReturn();
        assertEquals(result.getResponse().getContentAsString(), "User not found");
    }
}
