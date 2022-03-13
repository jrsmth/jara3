package com.jrsmiffy.jara3.userservice.service;

import com.jrsmiffy.jara3.userservice.model.User;
import com.jrsmiffy.jara3.userservice.model.UserResponse;
import com.jrsmiffy.jara3.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService underTest;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @BeforeEach
    void setUp() {
        underTest = new UserService(userRepository);
    }

    /** register() */
    @ParameterizedTest
    @CsvSource({"kingArthur, r0undT4bl3", "sirLancelot, br4v3"})
    @DisplayName("Should Register User")
    void shouldRegisterUser(final String username, final String password) {

        // Given: a valid potential user (that passes the checks)
        User validPotentialUser = new User(UUID.randomUUID(), username, password, true);

        // When: mock the repository call with Optional.empty & get the result from register()
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        UserResponse actualResult = underTest.register(validPotentialUser);

        // Then: check that this result is equal to the expected; register() should return the User
        then(userRepository).should().save(userArgumentCaptor.capture());
        User userArgumentCaptorValue = userArgumentCaptor.getValue();
        assertThat(userArgumentCaptorValue).isEqualTo(validPotentialUser);

        assertEquals(new UserResponse(Optional.of(validPotentialUser), "Registration Successful"), actualResult);
    }

    /** register() */
    @ParameterizedTest
    @CsvSource({"kingArthur, r0undT4bl3", "sirLancelot, br4v3"})
    @DisplayName("Should Not Register User - Username Already Exists")
    void shouldNotRegisterUserBecauseUsernameAlreadyExists(final String username, final String password) {

        // Given: an invalid potential user, where the username already exists (otherUser)
        User invalidPotentialUser = new User(UUID.randomUUID(), username, password, true);
        User otherUser = new User(UUID.randomUUID(), username, "password", true);

        // When: mock the repository call with otherUser & get the result from register()
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(otherUser));
        UserResponse actualResult = underTest.register(invalidPotentialUser);

        // Then: check that this result is equal to the expected; register() should return the User
        assertEquals(new UserResponse(Optional.empty(), String.format("Registration Failed: username '%s' already exists", username)), actualResult);
    }

    /** register() */
    @ParameterizedTest
    @CsvSource({
            "kingArthur,,missing username or password",
            ", br4v3,missing username or password"
    })
    @DisplayName("Should Not Register User - Invalid Credentials")
    void shouldNotRegisterUserBecauseInvalidCredentials(final String username, final String password, final String reason) {

        // Given: an invalid potential user, where the credentials are invalid
        User invalidPotentialUser = new User(UUID.randomUUID(), username, password, true);

        // When: repository mock not required; get the result from register()
        UserResponse actualResult = underTest.register(invalidPotentialUser);

        // Then: check that this result is equal to the expected; register() should return Optional.empty
        assertEquals(new UserResponse(Optional.empty(), "Registration Failed: " + reason), actualResult);
    }

    /** authenticate() */
    @ParameterizedTest
    @CsvSource({"kingArthur, r0undT4bl3", "sirLancelot, br4v3"})
    @DisplayName("Should Authenticate User")
    void shouldAuthenticateUser(final String username, final String password) {

        // Given: a valid user (that passes the checks)
        User validUser = new User(UUID.randomUUID(), username, password, true);
        UserResponse expectedResult = new UserResponse(Optional.of(validUser), "Authentication Successful");

        // When: mock the repository call with validUser & get the result from authenticate()
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(validUser));
        UserResponse actualResult = underTest.authenticate(username, password);

        // Then: check that this result is equal to the expected; authenticate() should return the User
        assertEquals(expectedResult, actualResult);
    }

    /** authenticate() */
    @ParameterizedTest
    @CsvSource({"kingArthur, r0undT4bl3", "sirLancelot, br4v3"})
    @DisplayName("Should Not Authenticate User - Username Doesn't Exist")
    void shouldNotAuthenticateUserBecauseUsernameDoesntExist(final String username, final String password) {

        // Given: a username that doesn't exist
            // from CSV

        // When: mock the repository call with Optional.empty & get the result from authenticate()
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        UserResponse actualResult = underTest.authenticate(username, password);

        // Then: check that this result is equal to the expected; authenticate() should return Optional.empty
        assertEquals(new UserResponse(Optional.empty(), String.format("Authentication Failed: user '%s' does not exist", username)), actualResult);
    }

    /** authenticate() */
    @ParameterizedTest
    @CsvSource({"kingArthur, r0undT4bl3", "sirLancelot, br4v3"})
    @DisplayName("Should Not Authenticate User - Incorrect Password")
    void shouldNotAuthenticateUserBecauseIncorrectPassword(final String username, final String password) {

        // Given: a valid username but a password that doesn't match
        String incorrectPassword = "password";
        User validUser = new User(UUID.randomUUID(), username, incorrectPassword, true);

        // When: mock the repository call with validUser & get the result from authenticate()
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(validUser));
        UserResponse actualResult = underTest.authenticate(username, password);

        // Then: check that this result is equal to the expected; authenticate() should return Optional.empty
        assertEquals(new UserResponse(Optional.empty(), "Authentication Failed: password doesn't match"), actualResult);

        verify(userRepository).findByUsername(username); // useful? when should use - investigate online...
    }

    /** authenticate() */
    @ParameterizedTest
    @CsvSource({
            "kingArthur,,missing username or password",
            ", br4v3,missing username or password"
    })
    @DisplayName("Should Not Authenticate User - Invalid Credentials")
    void shouldNotAuthenticateUserBecauseInvalidCredentials(final String username, final String password, final String reason) {

        // Given: an invalid username or password
            // from CSV

        // When: repository mock not required; get the result from authenticate()
        UserResponse actualResult = underTest.authenticate(username, password);

        // Then: check that this result is equal to the expected; authenticate() should return Optional.empty
        assertEquals(new UserResponse(Optional.empty(), "Authentication Failed: " + reason), actualResult);
    }


}