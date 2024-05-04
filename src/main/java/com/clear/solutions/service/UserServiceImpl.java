package com.clear.solutions.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.clear.solutions.model.User;
import com.clear.solutions.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User createUser(User user) {
        checkEmailIsAlreadyExisted(user.getEmail());
        userRepository.getUsers().add(user);
        return user;
    }

    @Override
    public User updateUser(String email, User updatedUser) {
        int userIndex = userRepository.getUsers().indexOf(getUserByEmail(email));
        userRepository.getUsers().set(userIndex, updatedUser);
        return updatedUser;
    }

    @Override
    public User patchUser(String email, Map<String, String> updates) {
        User existingUser = getUserByEmail(email);

        for (String key : updates.keySet()) {
            switch (key) {
                case "firstName" -> existingUser.setFirstName(updates.get(key));
                case "lastName" -> existingUser.setLastName(updates.get(key));
                case "birthDate" -> existingUser.setBirthDate(LocalDate.parse(updates.get(key)));
                case "address" -> existingUser.setAddress(updates.get(key));
                case "phoneNumber" -> existingUser.setPhoneNumber(updates.get(key));
                default -> log.info("User field: {} doesn't exist", key);
            }
        }
        return existingUser;
    }

    @Override
    public boolean deleteUser(String email) {
        return userRepository.getUsers().remove(getUserByEmail(email));
    }

    @Override
    public List<User> searchUsers(LocalDate from, LocalDate to) {
        if (from != null && to != null && from.isAfter(to)) {
            throw new IllegalArgumentException("From date must be before To date");
        }
        List<User> results = new ArrayList<>();
        for (User user : userRepository.getUsers()) {
            if ((from == null || user.getBirthDate().isAfter(from))
                    && (to == null || user.getBirthDate().isBefore(to))) {
                results.add(user);
            }
        }
        return results;
    }

    @Override
    public User getByEmail(String email) {
        int index = userRepository.getUsers().indexOf(getUserByEmail(email));
        return userRepository.getUsers().get(index);
    }

    private void checkEmailIsAlreadyExisted(String email) {
        if (userRepository.getUsers().stream()
                .anyMatch(u -> u.getEmail().equals(email))) {
            throw new IllegalArgumentException("User already exist, email:" + email);
        }
    }

    private User getUserByEmail(String email) {
        return userRepository.getUsers().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User not found, email: " + email));
    }
}
