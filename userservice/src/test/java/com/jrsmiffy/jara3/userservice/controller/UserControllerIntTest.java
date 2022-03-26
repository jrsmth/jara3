package com.jrsmiffy.jara3.userservice.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jrsmiffy.jara3.userservice.model.User;
import com.jrsmiffy.jara3.userservice.model.UserResponse;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @Test
    @Disabled
    @DisplayName("Test Register Endpoint")
    public void testRegisterEndpoint() throws Exception {

        // Given: a valid potential user (that passes the checks)
        User validPotentialUser = new User(UUID.randomUUID(), "username", "password", true);
        UserResponse userResponse = new UserResponse(Optional.of(validPotentialUser), "response");
        ResponseEntity<Object> expectedResponse = ResponseEntity.status(HttpStatus.OK).body(userResponse);

        // Then:
        mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"\",\"password\":\"password\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)))
                .andReturn();
    }

    @Test
//    @Disabled
    @DisplayName("Test Authenticate Endpoint")
    public void testAuthenticateEndpoint() throws Exception {

        // Given: a valid potential user (that passes the checks)
        User validPotentialUser = new User(UUID.randomUUID(), "username", "password", true);
        UserResponse userResponse = new UserResponse(Optional.of(validPotentialUser), "response");
        ResponseEntity<Object> expectedResponse = ResponseEntity.status(HttpStatus.OK).body(userResponse);

        // Then: check that this result is equal to the expected
        mockMvc.perform(get("/authenticate/username/password"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
//                        .andExpect(content().string(containsString("Hello, World")));

    }

}

//
//
//@ActiveProfiles("rest")
//@SpringBootTest
//@AutoConfigureMockMvc
//@RunWith(SpringRunner.class)
//public class RestTest {
//
//    @Autowired private MockMvc mockMvc;
//    @Autowired private RestTemplate restTemplate;
//
//    private MockRestServiceServer mockServer;
//
//    @Before
//    public void init() {
//        mockServer = createServer(restTemplate);
//    }
//
//    @Test
//    public void getsPersonById() throws Exception {
//        mockServer.expect(once(), requestTo(new URI("http://localhost:8080/employees/1")))
//                .andExpect(method(GET))
//                .andRespond(withStatus(OK)
//                        .contentType(APPLICATION_JSON)
//                        .body("{\"id\":\"1\",\"firstName\":\"Fred\",\"lastName\":\"Bloggs\",\"extn\":\"123\"}"));
//
//        mockMvc.perform(get("/api/v1/persons/1"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("Fred Bloggs"));
//    }
//}
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@AutoConfigureMockMvc
//public class ServiceTest {
//
//    @Autowired private MockMvc mockMvc;
//
//    @Test
//    public void getsPersonById() throws Exception {
//        mockMvc.perform(get("/api/v1/persons/1"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("Fred"));
//    }
//}
