package com.example.restapi.controller;

import com.example.restapi.model.User;
import com.example.restapi.util.JSONUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class UserController {
    private static final Map<Integer, User> users = new ConcurrentHashMap<>();
    private static final AtomicInteger nextId = new AtomicInteger(1);

    // Initialize with sample data
    static {
        users.put(nextId.getAndIncrement(), new User(1, "Alice Smith", "alice@example.com", 28));
        users.put(nextId.getAndIncrement(), new User(2, "Bob Johnson", "bob@example.com", 32));
        users.put(nextId.getAndIncrement(), new User(3, "Charlie Brown", "charlie@example.com", 24));
    }

    // Get all users
    public String getAllUsers() {
        List<User> userList = new ArrayList<>(users.values());
        return JSONUtils.toJSON(userList);
    }

    // Get user by ID
    public String getUserById(int id) {
        User user = users.get(id);
        if (user != null) {
            return JSONUtils.toJSON(user);
        } else {
            return null;
        }
    }

    // Create new user
    public String createUser(String jsonBody) {
        User user = JSONUtils.fromJSON(jsonBody, User.class);
        if (user != null) {
            int id = nextId.getAndIncrement();
            user.setId(id);
            users.put(id, user);
            return JSONUtils.toJSON(user);
        } else {
            return null;
        }
    }

    // Update user
    public String updateUser(int id, String jsonBody) {
        if (!users.containsKey(id)) {
            return null;
        }

        User updatedUser = JSONUtils.fromJSON(jsonBody, User.class);
        if (updatedUser != null) {
            User existingUser = users.get(id);
            existingUser.setName(updatedUser.getName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setAge(updatedUser.getAge());
            return JSONUtils.toJSON(existingUser);
        } else {
            return null;
        }
    }

    // Delete user
    public boolean deleteUser(int id) {
        return users.remove(id) != null;
    }

    // Get user count
    public int getUserCount() {
        return users.size();
    }
}
