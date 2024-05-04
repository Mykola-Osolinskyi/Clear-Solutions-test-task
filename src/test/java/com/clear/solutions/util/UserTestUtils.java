package com.clear.solutions.util;

import java.time.LocalDate;
import com.clear.solutions.model.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserTestUtils {
    private static final String EMAIL = "email@email.com";

    public static User buildDefaultUser() {
        return new User(
                EMAIL,
                "Bob",
                "Doe",
                LocalDate.of(2000, 10, 10),
                "123 Kyivska",
                "1234567890");
    }

    public static User buildUserWithUpdates() {
        return new User(
                EMAIL,
                "Bob-Updated",
                "Doe-Updated",
                LocalDate.of(2010, 10, 10),
                "123 Kyivska",
                "1234567890");
    }
}
