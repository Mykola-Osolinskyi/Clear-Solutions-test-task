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
    // todo: how to read value from property file?

    @Override
    public User createUser(User user) {
        if (isAdult(user)) {
            //todo: should we add another checks?
            userRepository.users.add(user);
            return user;
        } else {
            throw new IllegalArgumentException("User must be " + minAge + " years old or older");
        }
    }

    @Override
    public User updateUser(String email, User updatedUser) {
        if (!isAdult(updatedUser)) {
            return null;
        }
        int index = userRepository.users.indexOf(getUserByEmail(email));
        if (index != - 1) {
            userRepository.users.set(index, updatedUser);
            return updatedUser;
        } else {
            return null;
        }
    }

    @Override
    public User patchUser(String email, Map<String, Object> updates) {
        User existingUser = getUserByEmail(email);
        if (existingUser == null) {
            // todo:
            return null;
        }
        for (String key : updates.keySet()) {
            switch (key) {
                case "firstName":
                    existingUser.setFirstName((String) updates.get(key));
                    break;
                case "lastName":
                    existingUser.setLastName((String) updates.get(key));
                    break;
                case "birthDate":
                    // todo: need to validate age
//                    validateAge(updates.get(key));
                    existingUser.setBirthDate((LocalDate) updates.get(key));
                    break;
                case "address":
                    existingUser.setAddress((String) updates.get(key));
                    break;
                case "phoneNumber":
                    existingUser.setPhoneNumber((String) updates.get(key));
                    break;
                default:
                    // Handle unsupported fields (optional)
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

    private boolean isAdult(User user) {
        return LocalDate.now().minusYears(minAge).isBefore(user.getBirthDate());
    }

    private User getUserByEmail(String email) {
        return userRepository.users.stream().filter(u -> u.getEmail().equals(email)).findFirst()
                .orElseThrow(() -> new RuntimeException("Can't find user with email: "));
        // todo: add email to message
    }

}
