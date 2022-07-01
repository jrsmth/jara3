package com.jrsmiffy.jara3.userservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.jrsmiffy.jara3.userservice.model.AppUser;
import com.jrsmiffy.jara3.userservice.model.Role;
import com.jrsmiffy.jara3.userservice.repository.UserRepository;
import com.jrsmiffy.jara3.userservice.security.jwt.JwtUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private BCryptPasswordEncoder encoder;

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
        // prevent test leakage: database wipe is req. b.c usernames must be unique
    }

    @Test
    @DisplayName("Should Allow User Login")
    public void shouldAllowUserLogin() throws Exception {
        // Given: a valid user saved in the database
        final AppUser user = new AppUser(USERNAME, encoder.encode(PASSWORD)); // UUID auto-gen upon save
        this.userRepository.save(user);

        // Then: try to authenticate this user
        this.mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/login") // todo: remove spring profile urls, more hassle for testing?
                        .param("username", USERNAME)
                        .param("password", PASSWORD)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .servletPath("/api/login"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.access_token").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.refresh_token").exists());

        /*
            We want to decouple the test from the implementation to reduce test fragility,
            whilst still having a meaningful test. Instead of testing the specific response,
            we could just check that it contains the details that we want.
            How they're ordered is up to the implementation, we can check this in the unit tests.
         */
    }

    @Test
    @DisplayName("Should Not Allow User Login")
    public void shouldNotAllowUserLogin() throws Exception {
        // Given: an invalid user (not saved in the database, password mismatch, etc)

        // Then: try authenticating this user
        this.mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/login")
                        .param("username", USERNAME)
                        .param("password", PASSWORD))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("\"Bad credentials\""));
    }


    @Test
    @DisplayName("Should Register User")
    void shouldRegisterUser() throws Exception {
        // Given: a valid potential user (no invalid or duplicate credentials)

        // Then: try to register this user
        MvcResult result = this.mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/register")
                        .param("username", USERNAME)
                        .param("password", PASSWORD)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .servletPath("/api/register"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.user").exists())
                .andReturn();

        // Assert that the user was persisted
        final Object userMap = JsonPath.read(result.getResponse().getContentAsString(), "$.user");
        final AppUser returnedUser = objectMapper.convertValue(userMap, AppUser.class);

        final AppUser savedUser = userRepository.findByUsername(USERNAME).get();
        assertThat(returnedUser.getUsername()).isEqualTo(savedUser.getUsername());
        assertThat(returnedUser.getPassword()).isEqualTo(savedUser.getPassword());
        // todo: duplicate test logic: save for controller unit only? refactor... slim...
    }

    @Test
    @DisplayName("Should Not Register User")
    void shouldNotRegisterUser() throws Exception {
        // Given: an invalid user (already present in the database)
        this.userRepository.save(new AppUser(USERNAME, PASSWORD));

        // Then: try registering this user
        this.mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/register")
                        .param("username", USERNAME)
                        .param("password", PASSWORD)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .servletPath("/api/register"))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.user").isEmpty());
    }

    @Test
    @DisplayName("Should Get All Users")
    void shouldGetAllUsers() throws Exception {
        // Given: a user request with the ADMIN role
        String accessToken = jwtUtils.generateAccessToken(USERNAME, Role.ADMIN);

        // Then: try requesting all users
        this.mockMvc.perform(
                MockMvcRequestBuilders
                    .get("/api/admin/users")
                        .header("Authorization", "Bearer " + accessToken)
                        .servletPath("/api/admin/users"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should Not Get All Users Because Of Insufficient Role")
    void shouldNotGetAllUsersBecauseOfInsufficientRole() throws Exception {
        // Given: a user request without the ADMIN role
        String accessToken = jwtUtils.generateAccessToken(USERNAME, Role.USER);

        // Then: try requesting all users
        this.mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/admin/users")
                        .header("Authorization", "Bearer " + accessToken)
                        .servletPath("/api/admin/users"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should Get New Access Token")
    void shouldGetNewAccessToken() throws Exception {
        // Setup: ensure "admin" user exists in system
        if(userRepository.findByUsername("admin").isEmpty())
            this.userRepository.save(new AppUser(UUID.randomUUID(), "admin", "admin", Role.ADMIN, true));

        // Given: a valid refresh token for a user that exists in the system
        final String refreshToken = jwtUtils.generateRefreshToken("admin");

        // Then: try requesting a new access token
        MvcResult result = this.mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/token/refresh")
                        .header("Authorization", "Bearer " + refreshToken)
                        .servletPath("/api/token/refresh"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.access_token").exists())
                .andReturn();

        // Assert that the access token exists and is valid // todo: this check belongs in the controller (slim this test)
        final Object accessTokenObj = JsonPath.read(result.getResponse().getContentAsString(), "$.access_token");
        final String accessToken = objectMapper.convertValue(accessTokenObj, String.class);
        final String usernameAccessToken = jwtUtils.retrieveSubject(accessToken); // todo: JwtUtil/Controller unit test, could test exception thrown
        assertThat(usernameAccessToken).isEqualTo("admin");
    }

    @Test
    @DisplayName("Should Not Get New Access Token Because Of Invalid Refresh Token")
    void shouldNotGetNewAccessTokenBecauseOfInvalidRefreshToken() throws Exception {
        // Given: an invalid refresh token
        final String refreshToken = "insert_obviously_invalid_jwt";

        // Then: try requesting a new access token
        this.mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/token/refresh")
                        .header("Authorization", "Bearer " + refreshToken)
                        .servletPath("/api/token/refresh"))
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_message").exists());
    }

}
