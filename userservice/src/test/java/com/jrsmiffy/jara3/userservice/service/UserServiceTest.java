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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest // required to load @Value
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService underTest;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Value("${response.register.success}")
    private String responseRegisterSuccess;

    @Value("${response.register.fail.invalid-credentials}")
    private String responseRegisterFailInvalidCredentials;

    @Value("${response.register.fail.user-exists}")
    private String responseRegisterFailUserExists;

    @Value("${response.authenticate.success}")
    private String responseAuthenticateSuccess;

    @Value("${response.authenticate.fail.invalid-credentials}")
    private String responseAuthenticateFailInvalidCredentials;

    @Value("${response.authenticate.fail.no-user-exists}")
    private String responseAuthenticateFailNoUserExists;

    @Value("${response.authenticate.fail.incorrect-password}")
    private String responseAuthenticateFailIncorrectPassword;

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
        UserResponse expected = new UserResponse(Optional.of(validPotentialUser), responseRegisterSuccess);
        ReflectionTestUtils.setField(underTest, "responseRegisterSuccess", responseRegisterSuccess);

        // When:
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        UserResponse result = underTest.register(validPotentialUser);

        // Then:
        then(userRepository).should().save(userArgumentCaptor.capture());
        assertThat(result).isEqualTo(expected);

        User userArgumentCaptorValue = userArgumentCaptor.getValue();
        assertThat(userArgumentCaptorValue).isEqualTo(validPotentialUser);
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
        UserResponse expected = new UserResponse(Optional.empty(), responseRegisterFailInvalidCredentials + reason);
        ReflectionTestUtils.setField(underTest, "responseRegisterFailInvalidCredentials", responseRegisterFailInvalidCredentials);

        // When:
        UserResponse result = underTest.register(invalidPotentialUser);

        // Then:
        assertThat(result).isEqualTo(expected);
    }

    /** register() */
    @ParameterizedTest
    @CsvSource({"kingArthur, r0undT4bl3", "sirLancelot, br4v3"})
    @DisplayName("Should Not Register User - Username Already Exists")
    void shouldNotRegisterUserBecauseUsernameAlreadyExists(final String username, final String password) {

        // Given: an invalid potential user, where the username already exists (otherUser)
        User invalidPotentialUser = new User(UUID.randomUUID(), username, password, true);
        User otherUser = new User(UUID.randomUUID(), username, "password", true);
        UserResponse expected = new UserResponse(Optional.empty(), String.format(responseRegisterFailUserExists, username));
        ReflectionTestUtils.setField(underTest, "responseRegisterFailUserExists", responseRegisterFailUserExists);

        // When:
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(otherUser));
        UserResponse result = underTest.register(invalidPotentialUser);

        // Then:
        assertThat(result).isEqualTo(expected);
    }

    /** authenticate() */
    @ParameterizedTest
    @CsvSource({"kingArthur, r0undT4bl3", "sirLancelot, br4v3"})
    @DisplayName("Should Authenticate User")
    void shouldAuthenticateUser(final String username, final String password) {

        // Given: a valid user (that passes the checks)
        User validUser = new User(UUID.randomUUID(), username, password, true);
        UserResponse expected = new UserResponse(Optional.of(validUser), responseAuthenticateSuccess);
        ReflectionTestUtils.setField(underTest, "responseAuthenticateSuccess", responseAuthenticateSuccess);

        // When:
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(validUser));
        UserResponse result = underTest.authenticate(username, password);

        // Then:
        assertThat(result).isEqualTo(expected);
    }

    /** authenticate() */
    @ParameterizedTest
    @CsvSource({
            "kingArthur,,missing username or password",
            ", br4v3,missing username or password"
    })
    @DisplayName("Should Not Authenticate User - Invalid Credentials")
    void shouldNotAuthenticateUserBecauseInvalidCredentials(final String username, final String password, final String reason) {

        // Given: an invalid username or password from CSV
        UserResponse expected = new UserResponse(Optional.empty(), responseAuthenticateFailInvalidCredentials + reason);
        ReflectionTestUtils.setField(underTest, "responseAuthenticateFailInvalidCredentials", responseAuthenticateFailInvalidCredentials);

        // When:
        UserResponse result = underTest.authenticate(username, password); // repository mock not required

        // Then:
        assertThat(result).isEqualTo(expected);
    }

    /** authenticate() */
    @ParameterizedTest
    @CsvSource({"kingArthur, r0undT4bl3", "sirLancelot, br4v3"})
    @DisplayName("Should Not Authenticate User - Username Doesn't Exist")
    void shouldNotAuthenticateUserBecauseUsernameDoesntExist(final String username, final String password) {

        // Given: a username that doesn't exist from CSV
        UserResponse expected = new UserResponse(Optional.empty(), String.format(responseAuthenticateFailNoUserExists, username));
        ReflectionTestUtils.setField(underTest, "responseAuthenticateFailNoUserExists", responseAuthenticateFailNoUserExists);

        // When:
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        UserResponse result = underTest.authenticate(username, password);

        // Then:
        assertThat(result).isEqualTo(expected);
    }

    /** authenticate() */
    @ParameterizedTest
    @CsvSource({"kingArthur, r0undT4bl3", "sirLancelot, br4v3"})
    @DisplayName("Should Not Authenticate User - Incorrect Password")
    void shouldNotAuthenticateUserBecauseIncorrectPassword(final String username, final String password) {

        // Given: a valid username but a password that doesn't match
        String incorrectPassword = "password";
        User validUser = new User(UUID.randomUUID(), username, incorrectPassword, true);
        UserResponse expected = new UserResponse(Optional.empty(), responseAuthenticateFailIncorrectPassword);
        ReflectionTestUtils.setField(underTest, "responseAuthenticateFailIncorrectPassword", responseAuthenticateFailIncorrectPassword);

        // When:
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(validUser));
        UserResponse result = underTest.authenticate(username, password);

        // Then:
        assertThat(result).isEqualTo(expected);
        verify(userRepository).findByUsername(username); // when is it appropriate to use .verify()?
    }

}