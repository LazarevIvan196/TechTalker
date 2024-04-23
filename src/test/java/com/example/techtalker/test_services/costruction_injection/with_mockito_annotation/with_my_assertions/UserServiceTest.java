package com.example.techtalker.test_services.costruction_injection.with_mockito_annotation.with_my_assertions;

import com.example.techtalker.entity.Role;
import com.example.techtalker.entity.User;
import com.example.techtalker.custom.my_assert.UserAssert;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;

public class UserServiceTest {

    @Test
    void whenValidInput_thenRegistersUser() {
        User user = new User();
        user.setUsername("Пупкин Петька");
        user.setEmail("petka@example.com");
        user.setRoles(new HashSet<>(Collections.singletonList(new Role(1L, "USER_ROLE"))));

        UserAssert.assertThat(user)
                .hasUsername("Пупкин Петька")
                .hasEmail("petka@example.com")
                .hasAtLeastOneRole();
    }
}
