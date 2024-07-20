package com.ercandoygun.todoapp.controller;


import com.ercandoygun.todoapp.model.User;
import com.ercandoygun.todoapp.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
@Validated
@AllArgsConstructor
@Slf4j
public class UserController {

    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> create(@RequestBody @Valid User user) {
        log.info("Creating new user with name: " + user.getName());
        User createdUser = userService.registerUser(user);
        return ResponseEntity.ok(createdUser);
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        log.info("Getting all users");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody @Valid User user) {
        log.info("Updating user with id {}", user.getId());
        User updatedUser = userService.updateUser(user);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/update-password")
    public ResponseEntity<?> updatePassword(@RequestParam @NotNull String userId, @RequestParam String newPassword) {
        log.info("Updating password for user with id: {}", userId);
        User updatedUser = userService.updatePassword(userId, newPassword);
        return ResponseEntity.ok(updatedUser);
    }
}
