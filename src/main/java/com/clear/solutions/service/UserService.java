package com.clear.solutions.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import com.clear.solutions.model.User;

public interface UserService {

    User createUser(User user);

    User updateUser(String email, User updatedUser);

    User patchUser(String email, Map<String, String> updates);

    boolean deleteUser(String email);

    List<User> searchUsers(LocalDate from, LocalDate to);

    User getByEmail(String email);
}
