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

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    private UserController underTest;

    @BeforeEach
    void setUp() {
        underTest = new UserController(userService);
    }

    @Test
    @DisplayName("Should Register User")
    void shouldRegisterUser() {

        // Given: a valid potential user (that passes the checks)
        User validPotentialUser = new User(UUID.randomUUID(), "username", "password", true);
        UserResponse sampleResponse = new UserResponse(Optional.of(validPotentialUser), "response");
        ResponseEntity<UserResponse> expected = ResponseEntity.status(HttpStatus.OK).body(sampleResponse);

        // When:
        when(userService.register(validPotentialUser)).thenReturn(sampleResponse);
        ResponseEntity<UserResponse> result = underTest.register(validPotentialUser);

        // Then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("Should Not Register User - Checks Failed")
    void shouldNotRegisterUserBecauseChecksFailed() {

        // Given: an invalid potential user, where the checks fail
        User invalidPotentialUser = new User(UUID.randomUUID(), "username", "password", true);
        UserResponse sampleResponse = new UserResponse(Optional.empty(), "response");
        ResponseEntity<UserResponse> expected = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(sampleResponse);

        // When
        when(userService.register(invalidPotentialUser)).thenReturn(sampleResponse);
        ResponseEntity<UserResponse> result = underTest.register(invalidPotentialUser);

        // Then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("Should Authenticate User")
    void shouldAuthenticateUser() {

        // Given: a valid potential user (that passes the checks)
        User validPotentialUser = new User(UUID.randomUUID(), "username", "password", true);
        UserResponse sampleResponse = new UserResponse(Optional.of(validPotentialUser), "response");
        ResponseEntity<UserResponse> expected = ResponseEntity.status(HttpStatus.OK).body(sampleResponse);

        // When
        when(userService.authenticate("username", "password")).thenReturn(sampleResponse);
        ResponseEntity<UserResponse> result = underTest.authenticate("username", "password");

        // Then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("Should Not Authenticate User - Checks Failed")
    void shouldNotAuthenticateUserBecauseChecksFailed() {

        // Given: an invalid user, where the checks fail
        UserResponse sampleResponse = new UserResponse(Optional.empty(), "response");
        ResponseEntity<UserResponse> expected = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(sampleResponse);

        // When
        when(userService.authenticate("username", "password")).thenReturn(sampleResponse);
        ResponseEntity<UserResponse> result = underTest.authenticate("username", "password");

        // Then
        assertThat(result).isEqualTo(expected);
    }


}
