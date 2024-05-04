package com.clear.solutions.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import com.clear.solutions.model.User;
import com.clear.solutions.service.UserService;
import com.clear.solutions.service.UserValidatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userServiceImpl;
    private final UserValidatorService validatorService;


    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        validatorService.validateUser(user);
        User createdUser = userServiceImpl.createUser(user);
        return ResponseEntity.ok(createdUser);
    }

    @PutMapping("/{email}")
    public ResponseEntity<User> updateUser(@PathVariable String email, @RequestBody User user) {
        validatorService.validateUser(user);
        User updatedUser = userServiceImpl.updateUser(email, user);
        return updatedUser != null ? ResponseEntity.ok(updatedUser) : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{email}")
    public ResponseEntity<User> patchUser(@PathVariable String email, @RequestBody Map<String, String> updates) {
        validatorService.validateEmail(email);
        User user = userServiceImpl.patchUser(email, updates);
        validatorService.validateUser(user);
        return user != null ?  ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<?> deleteUser(@PathVariable String email) {
        validatorService.validateEmail(email);
        userServiceImpl.deleteUser(email);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam(required = false) LocalDate from, @RequestParam(required = false) LocalDate to) {
        if (from != null && to != null && from.isAfter(to)) {
            throw new IllegalArgumentException("From date must be before To date");
        }
        List<User> results = userServiceImpl.searchUsers(from, to);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/{email}")
    public ResponseEntity<User> getByEmail(@PathVariable String email) {
        validatorService.validateEmail(email);
        User user = userServiceImpl.getByEmail(email);
        return ResponseEntity.ok(user);
    }
}
