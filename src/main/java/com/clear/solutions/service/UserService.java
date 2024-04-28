package com.clear.solutions.service;

import com.clear.solutions.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface UserService {

    User createUser(User user);

    User updateUser(String email, User updatedUser);

    User patchUser(String email, Map<String, Object> updates);

    boolean deleteUser(String email);

    List<User> searchUsers(LocalDate from, LocalDate to);
}
