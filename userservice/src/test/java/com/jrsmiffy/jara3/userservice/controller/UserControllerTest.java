package com.jrsmiffy.jara3.userservice.controller;

import com.jrsmiffy.jara3.userservice.model.User;
import com.jrsmiffy.jara3.userservice.model.UserResponse;
import com.jrsmiffy.jara3.userservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    private UserController underTest;

    private ResponseEntity<Object> expectedResponse;

    private Map<HttpStatus, UserResponse> responseMap = new HashMap<>();

    @BeforeEach
    void setUp() {
        underTest = new UserController(userService);
    }

    @Test
    @DisplayName("Should Register User")
    void shouldRegisterUser() {

        // Given: a valid potential user (that passes the checks)
        User validPotentialUser = new User(UUID.randomUUID(), "username", "password", true);
        UserResponse mockedResponse = new UserResponse(Optional.of(validPotentialUser), "response");
        responseMap.put(HttpStatus.OK, mockedResponse);
        expectedResponse = ResponseEntity.ok(responseMap);

        // When: mock the service call with validPotentialUser & get the result from register()
        when(userService.register(validPotentialUser)).thenReturn(mockedResponse);
        ResponseEntity<Object> actualResult = underTest.register(validPotentialUser);

        // Then: check that this result is equal to the expected
        assertThat(actualResult).isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("Should Not Register User - Checks Failed")
    void shouldNotRegisterUserBecauseChecksFailed() {

        // Given: an invalid potential user, where the checks fail
        User invalidPotentialUser = new User(UUID.randomUUID(), "username", "password", true);
        UserResponse mockedResponse = new UserResponse(Optional.empty(), "response");
        responseMap.put(HttpStatus.CONFLICT, mockedResponse);
        expectedResponse = ResponseEntity.ok(responseMap);

        // When: mock the service call with invalid user & get the result from register()
        when(userService.register(invalidPotentialUser)).thenReturn(mockedResponse);
        ResponseEntity<Object> actualResult = underTest.register(invalidPotentialUser);

        // Then: check that this result is equal to the expected
        assertThat(actualResult).isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("Should Authenticate User")
    void shouldAuthenticateUser() {

        // Given: a valid potential user (that passes the checks)
        User validPotentialUser = new User(UUID.randomUUID(), "username", "password", true);
        UserResponse mockedResponse = new UserResponse(Optional.of(validPotentialUser), "response");
        responseMap.put(HttpStatus.OK, mockedResponse);
        expectedResponse = ResponseEntity.ok(responseMap);

        // When: mock the service call with validPotentialUser & get the result from authenticate()
        when(userService.authenticate("username", "password")).thenReturn(mockedResponse);
        ResponseEntity<Object> actualResult = underTest.authenticate("username", "password");

        // Then: check that this result is equal to the expected
        assertThat(actualResult).isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("Should Not Authenticate User - Checks Failed")
    void shouldNotAuthenticateUserBecauseChecksFailed() {

        // Given: an invalid potential user, where the checks fail
        User invalidPotentialUser = new User(UUID.randomUUID(), "username", "password", true);
        UserResponse mockedResponse = new UserResponse(Optional.empty(), "response");
        responseMap.put(HttpStatus.CONFLICT, mockedResponse);
        expectedResponse = ResponseEntity.ok(responseMap);

        // When: mock the service call with invalid user & get the result from authenticate()
        when(userService.authenticate("username", "password")).thenReturn(mockedResponse);
        ResponseEntity<Object> actualResult = underTest.authenticate("username", "password");

        // Then: check that this result is equal to the expected
        assertThat(actualResult).isEqualTo(expectedResponse);
    }


}
