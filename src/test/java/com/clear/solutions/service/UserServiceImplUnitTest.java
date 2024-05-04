package com.clear.solutions.service;

import static com.clear.solutions.util.UserTestUtils.*;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.clear.solutions.model.User;
import com.clear.solutions.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceImplUnitTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        User user = buildDefaultUser();
        List<User> users = new ArrayList<>();
        users.add(user);
        when(userRepository.getUsers()).thenReturn(users);
    }

    @AfterEach
    void tearDown() {
        userRepository.setUsers(emptyList());
    }


    @Test
    public void createUser_shouldReturnValidUser() {
        //given
        User user = buildDefaultUser();
        List<User> users = new ArrayList<>();
        when(userRepository.getUsers()).thenReturn(users);
        //when
        User userCreated = userService.createUser(user);
        //then
        assertNotNull(userCreated);
        assertEquals(user, userCreated);
    }

    @Test
    public void create_user_userWithSuchEmailAlreadyExists_shouldThrowException() {
        //given
        User user = buildDefaultUser();
        //when
        Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.createUser(user));
        //then
        assertEquals("User already exist, email:" + user.getEmail(), exception.getMessage());
    }

    @Test
    public void updateUser_shouldReturnValidUser() {
        //given
        User user = buildDefaultUser();
        String email = user.getEmail();
        User userWithUpdates = buildUserWithUpdates();
        //when
        User updatedUser = userService.updateUser(email, userWithUpdates);
        //then
        assertNotNull(updatedUser);
        assertEquals(userWithUpdates, updatedUser);
    }

    @Test
    public void updateUser_userDoesntExist_shouldThrowException() {
        //given
        String email = "new@email.com";
        User userWithUpdates = buildUserWithUpdates();
        //when
        Exception exception = assertThrows(RuntimeException.class, () -> userService.updateUser(email, userWithUpdates));
        //then
        assertEquals("User not found, email: " + email, exception.getMessage());
    }

    @Test
    public void patchUser_shouldReturnValidUser() {
        //given
        User user = buildDefaultUser();
        Map<String, String> updates = new HashMap<>();
        updates.put("firstName", "Billy");
        updates.put("lastName", "Smith");
        //when
        User patchedUser = userService.patchUser(user.getEmail(), updates);
        //then
        assertNotNull(patchedUser);
        assertEquals("Billy", patchedUser.getFirstName());
        assertEquals("Smith", patchedUser.getLastName());
    }

    @Test
    public void patchUser_userDoesntExist_shouldThrowException() {
        //given
        String email = "new@email.com";
        Map<String, String> updates = new HashMap<>();
        updates.put("firstName", "Billy");
        //when
        Exception exception = assertThrows(RuntimeException.class, () -> userService.patchUser(email, updates));
        //then
        assertEquals("User not found, email: " + email, exception.getMessage());
    }

    @Test
    public void deleteUser_shouldDeleteValidUser() {
        //given
        User user = buildDefaultUser();
        List<User> users = new ArrayList<>();
        users.add(user);
        when(userRepository.getUsers()).thenReturn(users);
        //when
        boolean result = userService.deleteUser(user.getEmail());
        //then
        assertTrue(result, "The user should have been successfully deleted.");
        assertFalse(users.contains(user));
    }

    @Test
    public void deleteUser_userDoesntExist_shouldThrowException() {
        //given
        String email = "new@email.com";
        //when
        Exception exception = assertThrows(RuntimeException.class, () -> userService.deleteUser(email));
        //then
        assertEquals("User not found, email: " + email, exception.getMessage());
    }

    @Test
    public void searchUsers_shouldReturnValidUsers() {
        //given
        User user = buildDefaultUser();
        List<User> users = new ArrayList<>();
        users.add(user);
        LocalDate from = LocalDate.of(2000, 1, 1);
        LocalDate to = LocalDate.of(2030, 12, 31);
        when(userRepository.getUsers()).thenReturn(users);
        //when
        List<User> searchedUsers = userService.searchUsers(from, to);
        //then
        assertNotNull(searchedUsers);
        assertEquals(users, searchedUsers);
    }

    @Test
    public void searchUsers_noUsersInDates_shouldReturnEmptyList() {
        //given
        LocalDate from = LocalDate.of(2025, 1, 1);
        LocalDate to = LocalDate.of(2030, 12, 31);
        //when
        List<User> searchedUsers = userService.searchUsers(from, to);
        //then
        assertEquals(0, searchedUsers.size());
    }

    @Test
    public void getByEmail_shouldReturnValidUser() {
        //given
        User user = buildDefaultUser();
        //when
        User userByEmail = userService.getByEmail(user.getEmail());
        //then
        assertNotNull(userByEmail);
        assertEquals(user, userByEmail);
    }
}