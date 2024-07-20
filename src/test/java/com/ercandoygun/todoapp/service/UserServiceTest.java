package com.ercandoygun.todoapp.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.ercandoygun.todoapp.repository.UserRepository;
import com.ercandoygun.todoapp.model.User;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("1", "Alice", "alice@example.com", "password", new Date(), new Date());
    }

    @Test
    public void getAllUsers_ReturnsListOfAllUsers() {
        List<User> expectedUsers = Arrays.asList(user, new User("2", "Bob", "bob@example.com", "password", new Date(), new Date()));

        when(userRepository.findAll()).thenReturn(expectedUsers);

        List<User> actualUsers = userService.getAllUsers();

        assertNotNull(actualUsers);
        assertEquals(2, actualUsers.size());
        assertEquals("Alice", actualUsers.get(0).getName());
    }

    @Test
    public void registerUser_EncodesPasswordAndSavesUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        User registeredUser = userService.registerUser(user);

        verify(passwordEncoder).encode("password");

        assertNotNull(registeredUser);
        assertEquals("encodedPassword", registeredUser.getPassword());
        assertEquals("Alice", registeredUser.getName());
    }

    @Test
    public void updateUser_UpdatesExistingUser() {
        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User updatedUser = userService.updateUser(new User("1", "AliceUpdated", "alice@example.com", "password", new Date(), new Date()));

        assertEquals("AliceUpdated", updatedUser.getName());
        verify(userRepository).save(user);
    }

    @Test
    public void updatePassword_UpdatesPasswordForExistingUser() {
        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedPassword");

        User userWithUpdatedPassword = userService.updatePassword("1", "newPassword");

        verify(passwordEncoder).encode("newPassword");
        assertEquals("encodedPassword", userWithUpdatedPassword.getPassword());
    }

    @Test
    public void updateUser_ThrowsExceptionWhenUserNotFound() {
        when(userRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.updateUser(user));
    }
}
