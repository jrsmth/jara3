package com.jrsmiffy.jara3.userservice.controller;


import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

//    @Test
//    @DisplayName("Test Register Endpoint")
//    public void testRegisterEndpoint() throws Exception {
//
//    }
//
//    @Test
//    @DisplayName("Test Authenticate Endpoint")
//    public void testAuthenticateEndpoint() throws Exception {
//
//    }
//
//    @Test
//    @DisplayName("Test Get All Users Endpoint")
//    public void testGetAllUsersEndpoint() throws Exception {
//
//    }

}
