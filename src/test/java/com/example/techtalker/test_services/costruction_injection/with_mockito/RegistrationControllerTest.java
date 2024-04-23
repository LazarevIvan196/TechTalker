package com.example.techtalker.test_services.costruction_injection.with_mockito;

import com.example.techtalker.controllers.RegistrationController;
import com.example.techtalker.dto.UserDto;
import com.example.techtalker.exception.DuplicateUserException;
import com.example.techtalker.services.UserService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = RegistrationController.class)
@WithMockUser(roles = "USER")
public class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;


    @Test
    void whenValidInput_thenRegistersUser() throws Exception {

        given(userService.saveUser(any(UserDto.class))).willReturn(true);

        mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", "testuser")
                        .param("email", "test@example.com")
                        .param("password", "password123")
                        .with(csrf()))

                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void whenDuplicateUser_thenRedirectsToRegistrationWithError() throws Exception {

        given(userService.saveUser(any(UserDto.class))).willThrow(DuplicateUserException.class);

        mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", "duplicateUser")
                        .param("email", "duplicate@example.com")
                        .param("password", "password123")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/registration"));
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public BCryptPasswordEncoder bCryptPasswordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }

}
