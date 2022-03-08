package com.jrsmiffy.jara3.userservice.controller;


import com.jrsmiffy.jara3.userservice.model.User;
import com.jrsmiffy.jara3.userservice.model.UserResponse;
import com.jrsmiffy.jara3.userservice.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
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

    @Test
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
        when(userService.register(validPotentialUser)).thenReturn(mockedResponse);

        // Then: check that this result is equal to the expected
        mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"username\",\"password\":\"password\"}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse.toString()))
                .andReturn();
    }

//    @Test
//    @DisplayName("Test Authenticate Endpoint")
//    public void testAuthenticateEndpoint() throws Exception {
//
//    }

}
