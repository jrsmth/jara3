package com.jrsmiffy.jara3.userservice;

import com.jrsmiffy.jara3.userservice.model.User;
import com.jrsmiffy.jara3.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceIT {

    @Autowired
    private WebApplicationContext applicationContext;

    @Autowired
    private UserRepository userRepository;

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
        User user = new User(UUID.randomUUID(), USERNAME, PASSWORD, true);
        this.userRepository.save(user);

        this.mockMvc.perform(
                get("/users/"+USERNAME+"/"+PASSWORD))
                .andExpect(status().isOk());
    }
}
