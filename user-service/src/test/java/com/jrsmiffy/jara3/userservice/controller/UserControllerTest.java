package com.jrsmiffy.jara3.userservice.controller;


import com.jrsmiffy.jara3.userservice.model.User;
import com.jrsmiffy.jara3.userservice.model.UserResponse;
import com.jrsmiffy.jara3.userservice.service.UserService;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@WebMvcTest
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    @Test
    @DisplayName("Should Authenticate User")
    public void shouldAuthenticateUser() throws Exception {
        // Given: a valid user ("saved in the database")
        User savedUser = new User(UUID.randomUUID(), USERNAME, PASSWORD, true);

        // When
        when(this.userService.authenticate(USERNAME, PASSWORD))
                .thenReturn(new UserResponse(Optional.of(savedUser), ""));

        // Then
        this.mockMvc.perform(get("/authenticate/{username}/{password}", USERNAME, PASSWORD))
                .andExpect(status().isOk());
//                .andExpect(jsonPath("name").value("prius")) TEST FOR THE EXACT RESPONSE!!!!
//                .andExpect(jsonPath("type").value("hybrid"));
    }


}
