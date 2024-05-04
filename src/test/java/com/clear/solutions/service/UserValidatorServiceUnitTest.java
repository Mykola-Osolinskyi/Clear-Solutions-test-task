package com.clear.solutions.service;

import static com.clear.solutions.util.UserTestUtils.buildDefaultUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import com.clear.solutions.exception.UserValidationException;
import com.clear.solutions.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = UserValidatorService.class)
@TestPropertySource(locations = "classpath:application.properties")
class UserValidatorServiceUnitTest {

    @Value("${user.minAge}")
    private int minAge;

    @InjectMocks
    private UserValidatorService userValidatorService;

    @BeforeEach
    public void setup() {
        System.out.println("minAge: " + minAge);
    }

    @Test
    public void validateEmail_emptyEmail_shouldThrowException() {
        //given
        User user = buildDefaultUser();
        user.setEmail("");
        //when
        Exception exception = assertThrows(UserValidationException.class, () -> userValidatorService.validateUser(user));
        //then
        assertEquals("Email is null or empty", exception.getMessage());
    }

    @Test
    public void validateEmail_invalidEmail_shouldThrowException() {
        //given
        User user = buildDefaultUser();
        user.setEmail("invalid.email");
        //when
        Exception exception = assertThrows(UserValidationException.class, () -> userValidatorService.validateUser(user));
        //then
        assertEquals("Email format is invalid", exception.getMessage());
    }

    @Test
    public void validateUser_nullUser_shouldThrowException() {
        //when
        Exception exception = assertThrows(UserValidationException.class, () -> userValidatorService.validateUser(null));
        //then
        assertEquals("User is null", exception.getMessage());
    }

    @Test
    public void validateUser_emptyName_shouldThrowException() {
        //given
        User user = buildDefaultUser();
        user.setFirstName("");
        //when
        Exception exception = assertThrows(UserValidationException.class, () -> userValidatorService.validateUser(user));
        //then
        assertEquals("First name or last name is null or empty", exception.getMessage());
    }

    @Test
    public void validateUser_invalidBirthDate_shouldThrowException() {
        //given
        User user = buildDefaultUser();
        user.setBirthDate(null);
        //when
        Exception exception = assertThrows(UserValidationException.class, () -> userValidatorService.validateUser(user));
        //then
        assertEquals("Birth date is invalid.", exception.getMessage());
    }

    @Test
    public void validateUser_ageLessThen18_shouldThrowException() {
        //given
        User user = buildDefaultUser();
        user.setBirthDate(LocalDate.now().minusYears(14));
        userValidatorService.minAge = minAge;
        //when
        Exception exception = assertThrows(UserValidationException.class, () -> userValidatorService.validateUser(user));
        //then
        assertEquals("User is less then " + minAge + " years", exception.getMessage());
    }
}
