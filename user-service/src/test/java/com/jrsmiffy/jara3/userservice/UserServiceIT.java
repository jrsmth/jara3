package com.jrsmiffy.jara3.userservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.jrsmiffy.jara3.userservice.model.User;
import com.jrsmiffy.jara3.userservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@Slf4j
public class UserServiceIT {

    @Autowired
    private WebApplicationContext applicationContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .build();
    }

    @Test
    @DisplayName("Should Authenticate User")
    void shouldAuthenticateUser() throws Exception {

        // Given: a valid user saved in the database
        final User user = new User(UUID.randomUUID(), USERNAME, PASSWORD, true);
        this.userRepository.save(user);

        // Then: try to authenticate this user
        this.mockMvc.perform(
                get("/authenticate/"+USERNAME+"/"+PASSWORD))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.user").value(user));

        /*
            We want to decouple the test from the implementation to reduce test fragility,
            whilst still having a meaningful test. Instead of testing the specific response,
            we could just check that it contains the details that we want.
            How they're ordered is up to the implementation, we can check this in the unit tests.
         */
    }

    @Test
    @DisplayName("Should Register User")
    void shouldRegisterUser() throws Exception {

        // Given: a valid potential user (no invalid or duplicate credentials)
        // USERNAME, PASSWORD

        // Then: try to register this user
        MvcResult result = this.mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/register")
                        .param("username", USERNAME)
                        .param("password", PASSWORD)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.user").exists())
                .andReturn();

        // Assert that the user was persisted
        final Object userMap = JsonPath.read(result.getResponse().getContentAsString(), "$.user");
        final User returnedUser = objectMapper.convertValue(userMap, User.class);
        log.info(returnedUser.toString());

        final User savedUser = userRepository.findByUsername(USERNAME).get();
        log.info(savedUser.toString());
        assertThat(returnedUser).isEqualTo(savedUser);
    }
}
