package com.clear.solutions.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import com.clear.solutions.model.User;
import com.clear.solutions.service.UserService;
import com.clear.solutions.service.UserValidatorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User management",
        description = "Endpoints for managing users")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userServiceImpl;
    private final UserValidatorService validatorService;

    @PostMapping
    @Operation(summary = "Create a new user", description = "Create a new user")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        validatorService.validateUser(user);
        User createdUser = userServiceImpl.createUser(user);
        return ResponseEntity.ok(createdUser);
    }

    @PutMapping("/{email}")
    @Operation(summary = "Update an existing user", description = "Update an existing user")
    public ResponseEntity<User> updateUser(@PathVariable String email,
                                           @RequestBody User user) {
        validatorService.validateUser(user);
        User updatedUser = userServiceImpl.updateUser(email, user);
        return updatedUser != null
                ? ResponseEntity.ok(updatedUser)
                : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{email}")
    @Operation(summary = "Update some user fields", description = "Update some user fields")
    public ResponseEntity<User> patchUser(@PathVariable String email,
                                          @RequestBody Map<String, String> updates) {
        validatorService.validateEmail(email);
        User user = userServiceImpl.patchUser(email, updates);
        validatorService.validateUser(user);
        return user != null
                ? ResponseEntity.ok(user)
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{email}")
    @Operation(summary = "Delete user", description = "Delete user")
    public ResponseEntity<?> deleteUser(@PathVariable String email) {
        validatorService.validateEmail(email);
        userServiceImpl.deleteUser(email);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Get list of users searched by birth date range",
            description = "Get list of users. Search for users by birth date range")
    public ResponseEntity<List<User>> searchUsers(@RequestParam(required = false) LocalDate from,
                                                  @RequestParam(required = false) LocalDate to) {
        if (from != null && to != null && from.isAfter(to)) {
            throw new IllegalArgumentException("From date must be before To date");
        }
        List<User> results = userServiceImpl.searchUsers(from, to);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/{email}")
    @Operation(summary = "Get user by email", description = "Get user by email")
    public ResponseEntity<User> getByEmail(@PathVariable String email) {
        validatorService.validateEmail(email);
        User user = userServiceImpl.getByEmail(email);
        return ResponseEntity.ok(user);
    }
}
