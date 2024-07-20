package com.ercandoygun.todoapp.service;

import com.ercandoygun.todoapp.model.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();
    User registerUser(User user);
    User updateUser(User user);
    User updatePassword(String userId, String newPassword);
}
