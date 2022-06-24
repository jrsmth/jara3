package com.jrsmiffy.jara3.userservice.controller;

import com.jrsmiffy.jara3.userservice.model.AppUser;
import com.jrsmiffy.jara3.userservice.model.Role;
import com.jrsmiffy.jara3.userservice.model.UserResponse;
import com.jrsmiffy.jara3.userservice.service.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    // inspiration: https://stackoverflow.com/questions/68133634/how-to-unit-test-a-controller-method-with-spring-boot-and-mockito

    @InjectMocks
    private UserController underTest;

    @Mock
    private UserServiceImpl mockService;

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    @Test
    @DisplayName("Should Authenticate User")
    void shouldAuthenticateUser() {
        // Given: a valid user ("saved in the database")
        AppUser savedUser = new AppUser(UUID.randomUUID(), USERNAME, PASSWORD, Role.USER,true);

        // When
        when(mockService.authenticate(USERNAME, PASSWORD))
                .thenReturn(new UserResponse(Optional.of(savedUser), ""));
        // no need to focus on what the response is here, this is the responsibility of the user svc

        ResponseEntity<UserResponse> actual = underTest.authenticate(USERNAME, PASSWORD);

        // Then
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual.getBody().getUser().get().getUsername()).isEqualTo(USERNAME);
        assertThat(actual.getBody().getUser().get().getPassword()).isEqualTo(PASSWORD);
        assertThat(actual.getBody().getResponse()).isNotNull();

        verify(mockService).authenticate(USERNAME, PASSWORD);
    }

    @Test
    @DisplayName("Should Not Authenticate User")
    void shouldNotAuthenticateUser() {
        // Given: an invalid user ("not saved in database", etc)

        // When
        when(mockService.authenticate(USERNAME, PASSWORD))
                .thenReturn(new UserResponse(Optional.empty(), ""));

        ResponseEntity<UserResponse> actual = underTest.authenticate(USERNAME, PASSWORD);

        // Then
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(actual.getBody().getUser()).isEmpty();
        assertThat(actual.getBody().getResponse()).isNotNull();

        verify(mockService).authenticate(USERNAME, PASSWORD);
    }

    @Test
    @DisplayName("Should Register User")
    void shouldRegisterUser() {
        // Given: a valid potential user (no invalid or duplicate credentials)
        // USERNAME, PASSWORD

        // When
        when(mockService.register(USERNAME, PASSWORD))
                .thenReturn(new UserResponse(Optional.of(new AppUser(USERNAME, PASSWORD)),""));

        ResponseEntity<UserResponse> actual = underTest.register(USERNAME, PASSWORD);

        // Then
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual.getBody().getUser().get().getUsername()).isEqualTo(USERNAME);
        assertThat(actual.getBody().getUser().get().getPassword()).isEqualTo(PASSWORD);
        assertThat(actual.getBody().getResponse()).isNotNull();

        verify(mockService).register(USERNAME, PASSWORD);
    }

    @Test
    @DisplayName("Should Not Register User")
    void shouldNotRegisterUser() {
        // Given: an invalid user (already present in the database, invalid credentials, etc)

        // When
        when(mockService.register(USERNAME, PASSWORD))
                .thenReturn(new UserResponse(Optional.empty(),""));

        ResponseEntity<UserResponse> actual = underTest.register(USERNAME, PASSWORD);

        // Then:
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(actual.getBody().getUser()).isEmpty();
        assertThat(actual.getBody().getResponse()).isNotNull();

        verify(mockService).register(USERNAME, PASSWORD);
    }

    @Test
    @DisplayName("Should Get All Users") // DEV USE ONLY
    void shouldGetAllUsers() {
        // Given: a user "saved" in the database
        AppUser savedUser = new AppUser(UUID.randomUUID(), USERNAME, PASSWORD, Role.USER,true);

        // When
        when(mockService.getAllUsers())
                .thenReturn(List.of(savedUser));

        // Then
        assertThat(underTest.getAllUsers())
                .isEqualTo(List.of(savedUser));

        verify(mockService).getAllUsers();
    }
}
