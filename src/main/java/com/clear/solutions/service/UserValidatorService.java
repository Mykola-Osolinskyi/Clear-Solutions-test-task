package com.clear.solutions.service;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.clear.solutions.exception.UserValidationException;
import com.clear.solutions.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserValidatorService {
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Value("${user.minAge}")
    public int minAge;

    public void validateUser(User user) {
        if (user == null) {
            throw new UserValidationException("User is null");
        }
        validateEmail(user.getEmail());
        validateName(user);
        validateAge(user.getBirthDate());
    }

    public void validateEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new UserValidationException("Email is null or empty");
        }

        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        if (!matcher.matches()) {
            throw new UserValidationException("Email format is invalid");
        }
    }

    private void validateName(User user) {
        if (isNullOrEmpty(user.getFirstName()) || isNullOrEmpty(user.getLastName())) {
            throw new UserValidationException("First name or last name is null or empty");
        }
    }

    private void validateAge(LocalDate birthDate) {
        if (birthDate == null || LocalDate.now().isBefore(birthDate)) {
            throw new UserValidationException("Birth date is invalid.");
        }

        if (LocalDate.now().minusYears(minAge).isBefore(birthDate)) {
            throw new UserValidationException("User is less then " + minAge + " years");
        }
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.isEmpty();
    }
}
