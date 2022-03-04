package com.jrsmiffy.jara3.userservice.controller;


import com.jrsmiffy.jara3.userservice.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerIntegrationTest { // integration tests

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("Test Register Endpoint")
    public void testRegisterEndpoint() throws Exception {

//        // given
//        String inputString = "abc";
//        String expectedResult = "cba";
//
//        // when
//        when(userService.authenticate(inputString, "helpme")).thenReturn(null);
//
//        // then
//        mockMvc.perform(get("/reverse").param("input", inputString))
//                .andExpect(status().isOk())
//                .andExpect(content().string(expectedResult));
    }

    @Test
    @DisplayName("Test Authenticate Endpoint")
    public void testAuthenticateEndpoint() throws Exception {


    }

    @Test
    @DisplayName("Test Get All Users Endpoint")
    public void testGetAllUsersEndpoint() throws Exception {
        mockMvc.perform( get("/users")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.users").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.users[*].id").isNotEmpty());
    }

}
