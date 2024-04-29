package com.clear.solutions.service;

import com.clear.solutions.model.User;
import com.clear.solutions.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Value("${user.minAge}")
    private int minAge;

    @Override
    public User createUser(User user) {
        checkUser(user);
        userRepository.users.add(user);
        return user;
    }

    @Override
    public User updateUser(String email, User updatedUser) {
        checkUser(updatedUser);
        int index = userRepository.users.indexOf(getUserByEmail(email));
        userRepository.users.set(index, updatedUser);
        return updatedUser;
    }

    @Override
    public User patchUser(String email, Map<String, Object> updates) {
        User existingUser = getUserByEmail(email);

        for (String key : updates.keySet()) {
            switch (key) {
                case "firstName" -> existingUser.setFirstName((String) updates.get(key));
                case "lastName" -> existingUser.setLastName((String) updates.get(key));
                case "birthDate" -> existingUser.setBirthDate((LocalDate) updates.get(key)); // todo: need to check user age
                case "address" -> existingUser.setAddress((String) updates.get(key));
                case "phoneNumber" -> existingUser.setPhoneNumber((String) updates.get(key));
                default -> {
                    // Handle unsupported fields (optional)
                }
            }
        }
        return existingUser;
    }

    @Override
    public boolean deleteUser(String email) {
        return userRepository.users.remove(getUserByEmail(email));
    }

    @Override
    public List<User> searchUsers(LocalDate from, LocalDate to) {
        if (from != null && to != null && from.isAfter(to)) {
            throw new IllegalArgumentException("From date must be before To date");
        }
        List<User> results = new ArrayList<>();
        for (User user : userRepository.users) {
            if ((from == null || user.getBirthDate().isAfter(from)) && (to == null || user.getBirthDate().isBefore(to))) {
                results.add(user);
            }
        }
        return results;
    }

    private void checkUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User must not be null");
        }
        if (!LocalDate.now().minusYears(minAge).isBefore(user.getBirthDate())) {
            throw new IllegalArgumentException("User must be " + minAge + " years old or older");
        }
    }

    private User getUserByEmail(String email) {
        return userRepository.users.stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Can't find user with email: " + email));
    }
}
