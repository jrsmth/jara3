package com.jrsmiffy.jara3.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jrsmiffy.jara3.userservice.model.User;
import com.jrsmiffy.jara3.userservice.model.UserResponse;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

//    @MockBean
//    private UserService userService; // give null pointer, as is

    private static final Logger log = LoggerFactory.getLogger(UserControllerIntTest.class);

    @Value("${response.register.success}")
    private String responseRegisterSuccess;

    @Value("${response.authenticate.success}")
    private String responseAuthenticateSuccess;

    @Test
//    @Disabled
    @DisplayName("Test Register Endpoint")
    public void testRegisterEndpoint() throws Exception {

        // Given: a valid user (that passes the checks)
        String uuid = "10101010-1010-1010-1010-101010101010";
        User userValid = new User(UUID.fromString(uuid), "username", "password", true);
        UserResponse expectedResponse = new UserResponse(Optional.of(userValid), responseRegisterSuccess);

        // When: the UUID is randomly generated, so we need to make this static for assertion
        // ........

        // Then:
        String body = String.format("{\"username\": \"%s\",\"password\":\"%s\"}",
                userValid.getUsername(),
                userValid.getPassword());
        mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)))
                .andReturn();
    }

    @Test
//    @Disabled
    @DisplayName("Test Authenticate Endpoint")
    public void testAuthenticateEndpoint() throws Exception {

        // Given: a valid user (that passes the checks)
        String uuid = "10101010-1010-1010-1010-101010101010";
        User userValid = new User(UUID.fromString(uuid), "username1", "password1", true);
        UserResponse expectedResponse = new UserResponse(Optional.of(userValid), responseAuthenticateSuccess);

        // Then: check that this result is equal to the expected
        mockMvc.perform(get("/authenticate/username1/password1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)))
                .andReturn();
    }

    /** this class is not being executed with mvn test! - investigate
     * all tests are being run bar this Int class!
     *          Also @Disabled is being ignored
     */

}