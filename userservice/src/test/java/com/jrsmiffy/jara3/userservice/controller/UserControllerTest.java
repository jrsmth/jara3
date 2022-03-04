package com.jrsmiffy.jara3.userservice.controller;

import com.google.gson.Gson;
import com.jrsmiffy.jara3.userservice.service.UserService;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest { // integration tests

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("Test Register Endpoint")
    public void testRegisterEndpoint() throws Exception {

        // given
        String inputString = "abc";
        String expectedResult = "cba";

        // when
        when(stringParsingService.reverseString(inputString)).thenReturn(expectedResult);

        // then
        mockMvc.perform(get("/reverse").param("input", inputString))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResult));
    }

    @Test
    @DisplayName("Test Authenticate Endpoint")
    public void testAuthenticateEndpoint() throws Exception {

        
    }

}
