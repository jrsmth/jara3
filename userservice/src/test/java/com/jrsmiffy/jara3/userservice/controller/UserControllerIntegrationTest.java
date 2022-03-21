package com.jrsmiffy.jara3.userservice.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jrsmiffy.jara3.userservice.model.User;
import com.jrsmiffy.jara3.userservice.model.UserResponse;
import com.jrsmiffy.jara3.userservice.service.UserService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Disabled
    @DisplayName("Test Register Endpoint")
    public void testRegisterEndpoint() throws Exception {

        // Given: a valid potential user (that passes the checks)
        User validPotentialUser = new User(UUID.randomUUID(), "username", "password", true);
        UserResponse mockedResponse = new UserResponse(Optional.of(validPotentialUser), "response");

        ResponseEntity<Object> expectedResponse;
        Map<HttpStatus, UserResponse> responseMap = new HashMap<>();
        responseMap.put(HttpStatus.OK, mockedResponse);
        expectedResponse = ResponseEntity.ok(responseMap);

        // When: mock the service call with validPotentialUser
        when(userService.register(ArgumentMatchers.any(User.class))).thenReturn(mockedResponse);

        // Then: check that this result is equal to the expected
        mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"username\",\"password\":\"password\"}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)))
                .andReturn();
    }

    @Test
    @Disabled
    @DisplayName("Test Authenticate Endpoint")
    public void testAuthenticateEndpoint() throws Exception {

        // Given: a valid potential user (that passes the checks)
        User validPotentialUser = new User(UUID.randomUUID(), "username", "password", true);
        UserResponse mockedResponse = new UserResponse(Optional.of(validPotentialUser), "response");
        ResponseEntity<Object> expectedResponse;
        Map<HttpStatus, UserResponse> responseMap = new HashMap<>();
        responseMap.put(HttpStatus.OK, mockedResponse);
        expectedResponse = ResponseEntity.ok(responseMap);

        // When: mock the service call with validPotentialUser & get the result from authenticate()
        when(userService.authenticate("username", "password")).thenReturn(mockedResponse);

        // Then: check that this result is equal to the expected
        mockMvc.perform(get("/authenticate/username/password"))
                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));

        // TODO - tech ref update
        // TODO - use verify where appropriate - find out where this is and add it to TINTK - what has stackoverflow got to say...
        //import org.junit.jupiter.api.Test; giving me errors here; right up on Teams
        //import org.junit.Test; works like a charm for int tests, is juniper specific to unit tests?
        // TODO - can we import responses from config file???
        // TODO - then finally do integration testing!
        // TODO - @value null in unit test (explanation: https://stackoverflow.com/questions/57436788/value-returning-null-in-unit-test)
            // https://stackoverflow.com/questions/23162777/how-do-i-mock-an-autowired-value-field-in-spring-with-mockito
            //https://stackoverflow.com/questions/38711871/load-different-application-yml-in-springboot-test - @SpringBootTest, nec. evil
    }

}