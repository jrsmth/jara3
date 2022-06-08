package com.jrsmiffy.jara3.userservice.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
        when(userService.authenticate(USERNAME, PASSWORD))
                .thenReturn(new UserResponse(Optional.of(savedUser), ""));
        // no need to focus on what the response is here, this is the responsibility of the user svc

        // Then
        MvcResult result = this.mockMvc.perform(get("/authenticate/{username}/{password}", USERNAME, PASSWORD))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.user").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response").exists())
                .andReturn();

        final Object userMap = JsonPath.read(result.getResponse().getContentAsString(), "$.user");
        final User returnedUser = objectMapper.convertValue(userMap, User.class);
        assertThat(returnedUser).isEqualTo(savedUser);
    }

    @Test
    @DisplayName("Should Not Authenticate User")
    public void shouldNotAuthenticateUser() throws Exception {
        // Given: an invalid user ("not saved in database", etc)

        // When
        when(userService.authenticate(USERNAME, PASSWORD))
                .thenReturn(new UserResponse(Optional.empty(), ""));

        // Then
        this.mockMvc.perform(get("/authenticate/{username}/{password}", USERNAME, PASSWORD))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.user").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response").exists());
    }


}
