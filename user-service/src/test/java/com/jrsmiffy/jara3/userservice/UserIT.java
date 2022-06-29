package com.jrsmiffy.jara3.userservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.jrsmiffy.jara3.userservice.model.AppUser;
import com.jrsmiffy.jara3.userservice.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
class UserIT {

    @Autowired
    private WebApplicationContext applicationContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private JwtEndpointAccessTokenGenerator jwtTokenGenerator;

    private MockMvc mockMvc;

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @AfterEach
    void tearDown() {
        this.userRepository.deleteAll();
        // wiping the database is required because usernames must be unique
    }

//    @Test
//    @DisplayName("Should Authenticate User")
//    void shouldAuthenticateUser() throws Exception {
//
//        // Given: a valid user saved in the database
//        final AppUser user = new AppUser(USERNAME, PASSWORD); // UUID auto-gen upon save
//        this.userRepository.save(user);
//
//        // Then: try to authenticate this user
//        this.mockMvc.perform(
//                get("/api/authenticate/{username}/{password}", USERNAME, PASSWORD)) // TODO: use the spring profile urls
//                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.user.username").value(user.getUsername()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.user.password").value(user.getPassword()));
//
//        /*
//            We want to decouple the test from the implementation to reduce test fragility,
//            whilst still having a meaningful test. Instead of testing the specific response,
//            we could just check that it contains the details that we want.
//            How they're ordered is up to the implementation, we can check this in the unit tests.
//         */
//    }
//
//    @Test
//    @DisplayName("Should Not Authenticate User")
//    void shouldNotAuthenticateUser() throws Exception {
//        // Given: an invalid user (not saved in the database, password mismatch, etc)
//
//        // Then: try authenticating this user
//        this.mockMvc.perform(
//                get("/api/authenticate/{username}/{password}", USERNAME, PASSWORD))
//                .andExpect(status().isBadRequest())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.user").isEmpty());
//    }


    @Test
    @DisplayName("Should Register User")
    void shouldRegisterUser() throws Exception {

        // Given: a valid potential user (no invalid or duplicate credentials)
        // USERNAME, PASSWORD

        // Then: try to register this user
        MvcResult result = this.mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/register")
                        .param("username", USERNAME)
                        .param("password", PASSWORD)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.user").exists())
                .andReturn();

        // Assert that the user was persisted
        final Object userMap = JsonPath.read(result.getResponse().getContentAsString(), "$.user");
        final AppUser returnedUser = objectMapper.convertValue(userMap, AppUser.class);

        final AppUser savedUser = userRepository.findByUsername(USERNAME).get();
        assertThat(returnedUser.getUsername()).isEqualTo(savedUser.getUsername());
        assertThat(returnedUser.getPassword()).isEqualTo(savedUser.getPassword());
    }

    @Test
    @DisplayName("Should Not Register User")
    void shouldNotRegisterUser() throws Exception {
        // Given: an invalid user (already present in the database, invalid credentials, etc)
        this.userRepository.save(new AppUser(USERNAME, PASSWORD));

        // Then: try registering this user
        this.mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/register")
                        .param("username", USERNAME)
                        .param("password", PASSWORD)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.user").isEmpty());
    }

    @Test
    @DisplayName("Should Get All Users")
    @WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
    void shouldGetAllUsers() throws Exception {
        // Given: a user request with the ADMIN role

        // Then: try requesting all users
        this.mockMvc.perform(
                MockMvcRequestBuilders
                    .get("/api/admin/users"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should Not Get All Users Because Of Insufficient Role")
    @WithMockUser(username = "user", password = "user", roles = "USER")
    void shouldNotGetAllUsersBecauseOfInsufficientRole() throws Exception {
        // Given: a user request without the ADMIN role

        // Then: try requesting all users
        this.mockMvc.perform( //todo: see the accepted answer: HERE! https://stackoverflow.com/questions/70262617/how-to-mock-jwt-token-to-use-it-with-mockito-and-spring-boot
                MockMvcRequestBuilders
                        .get("/api/admin/users")
                        .header("Authorization", "Bearer token from @MOckUser"))
                .andExpect(status().isForbidden()); // no auth header is set ffs...
    }

}
