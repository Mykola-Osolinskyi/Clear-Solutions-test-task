package com.clear.solutions.controller;

import static com.clear.solutions.util.UserTestUtils.buildDefaultUser;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.clear.solutions.exception.UserValidationException;
import com.clear.solutions.model.User;
import com.clear.solutions.service.UserService;
import com.clear.solutions.service.UserValidatorService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
class UserControllerIntegrationTest {
    private static final String NOT_EXISTING_EMAIL = "notExistingEmail";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserValidatorService validatorService;

    @Test
    void test_createUser_validInput() throws Exception {
        //given
        User user = buildDefaultUser();
        String jsonUser = mapUserToJson(user);
        //when
        when(userService.createUser(any(User.class))).thenReturn(user);
        //then
        mvc.perform(post("/users")
                        .contentType(APPLICATION_JSON)
                        .content(jsonUser))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(user.getFirstName())));
    }

    @Test
    void test_createUser_invalidInput_wrongEmail() throws Exception {
        //given
        User user = buildDefaultUser();
        user.setEmail(NOT_EXISTING_EMAIL);
        String jsonUser = mapUserToJson(user);
        //when
        doThrow(new UserValidationException("Email format is invalid"))
                .when(validatorService).validateUser(any(User.class));
        //then
        mvc.perform(post("/users")
                        .contentType(APPLICATION_JSON)
                        .content(jsonUser))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void test_getUser_validInput() throws Exception {
        //given
        User user = buildDefaultUser();
        String email = user.getEmail();
        //when
        when(userService.getByEmail(anyString())).thenReturn(user);
        //then
        mvc.perform(get("/users/" + email))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.email", is(email)))
                .andExpect(jsonPath("$.firstName", is("Bob")))
                .andExpect(jsonPath("$.lastName", is("Doe")));
    }

    @Test
    public void test_getUser_invalidInput_userWithSuchEmailNotExist() throws Exception {
        //given
        String email = NOT_EXISTING_EMAIL;
        //when
        when(userService.getByEmail(anyString()))
                .thenThrow(new RuntimeException("User not found, email: " + email));
        //then
        mvc.perform(get("/users/" + email))
                .andExpect(status().isNotFound());
    }

    @Test
    public void test_updateUser_validInput() throws Exception {
        //given
        User user = buildDefaultUser();
        String email = user.getEmail();
        String jsonUser = mapUserToJson(user);
        //when
        when(userService.updateUser(anyString(), any(User.class))).thenReturn(user);
        //then
        mvc.perform(put("/users/" + email)
                        .contentType(APPLICATION_JSON)
                        .content(jsonUser))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(user.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(user.getLastName())));
    }

    @Test
    public void test_updateUser_invalidInput_userIsNotAdult() throws Exception {
        //given
        User user = buildDefaultUser();
        String email = user.getEmail();
        String jsonUser = mapUserToJson(user);
        //when
        doThrow(new UserValidationException("User is less then 18 years"))
                .when(validatorService).validateUser(any(User.class));
        //then
        mvc.perform(put("/users/" + email)
                        .contentType(APPLICATION_JSON)
                        .content(jsonUser))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void test_deleteUser_validInput() throws Exception {
        //given
        User user = buildDefaultUser();
        String email = user.getEmail();
        //when
        when(userService.deleteUser(email)).thenReturn(true);
        //then
        mvc.perform(delete("/users/" + email))
                .andExpect(status().isOk());
    }

    @Test
    public void test_deleteUser_invalidInput_userWithSuchEmailNotExist() throws Exception {
        //given
        String email = NOT_EXISTING_EMAIL;
        //when
        doThrow(new RuntimeException("User not found,  email: " + email))
                .when(userService).deleteUser(anyString());
        //then
        mvc.perform(delete("/users/" + email))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("User not found")));
    }

    @Test
    public void test_patchUser_validInput() throws Exception {
        //given
        User user = buildDefaultUser();
        String email = user.getEmail();
        Map<String, String> updates = new HashMap<>();
        updates.put("firstName", "NewBob");
        updates.put("lastName", "NewDoe");
        String jsonUpdates = new ObjectMapper().writeValueAsString(updates);
        //when
        when(userService.patchUser(anyString(), any())).thenReturn(user);
        //then
        mvc.perform(patch("/users/" + email)
                        .contentType(APPLICATION_JSON)
                        .content(jsonUpdates))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(user.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(user.getLastName())));
    }

    @Test
    public void test_patchUser_invalidInput_emptyFirstName() throws Exception {
        //given
        User user = buildDefaultUser();
        String email = user.getEmail();
        String jsonUser = mapUserToJson(user);
        //when
        doThrow(new UserValidationException("First name or last name is null or empty"))
                .when(validatorService).validateUser(user);
        //then
        mvc.perform(patch("/users/" + email)
                        .contentType(APPLICATION_JSON)
                        .content(jsonUser))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void test_searchUsers_validInput() throws Exception {
        //given
        User user = buildDefaultUser();
        List<User> users = new ArrayList<>();
        users.add(user);
        LocalDate from = LocalDate.of(1920, 1, 1);
        LocalDate to = LocalDate.of(2030, 1, 1);
        String jsonFrom = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(from);
        String jsonTo = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(to);
        //when
        when(userService.searchUsers(any(), any()))
                .thenReturn(users);
        //then
        mvc.perform(get("/users/search")
                        .contentType(APPLICATION_JSON)
                        .content(jsonFrom)
                        .content(jsonTo))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    public void test_searchUsers_invalidInput_fromBiggerThenTo() throws Exception {
        //given
        LocalDate from = LocalDate.of(2030, 1, 1);
        LocalDate to = LocalDate.of(1920, 1, 1);
        String jsonFrom = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(from);
        String jsonTo = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(to);
        //when
        doThrow(new IllegalArgumentException("From date must be before To date"));
        //then
        mvc.perform(get("/users/search")
                .contentType(APPLICATION_JSON)
                .content(jsonFrom)
                .content(jsonTo))
                .andExpect(status().isBadRequest());
    }

    private String mapUserToJson(User user) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        return objectMapper.writeValueAsString(user);
    }
}
