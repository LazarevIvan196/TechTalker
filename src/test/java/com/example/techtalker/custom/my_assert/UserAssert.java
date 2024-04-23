package com.example.techtalker.custom.my_assert;

import com.example.techtalker.entity.User;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserAssert {

    private final User user;

    public UserAssert(User user) {
        this.user = user;
    }

    public static UserAssert assertThat(User actual) {
        return new UserAssert(actual);
    }

    public UserAssert hasUsername(String expectedUsername) {
        assertEquals(expectedUsername, user.getUsername());
        return this;
    }

    public UserAssert hasEmail(String expectedEmail) {
        assertEquals(expectedEmail, user.getEmail());
        return this;
    }

    public UserAssert hasAtLeastOneRole() {
        assertEquals(1, user.getRoles().size());
        return this;
    }
}
