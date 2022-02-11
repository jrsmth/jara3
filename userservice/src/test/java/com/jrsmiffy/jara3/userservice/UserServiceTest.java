package com.jrsmiffy.jara3.userservice;

import com.jrsmiffy.jara3.userservice.model.User;
import com.jrsmiffy.jara3.userservice.repository.UserRepository;
import com.jrsmiffy.jara3.userservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;
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

    @ParameterizedTest
    @CsvSource({"smith,james", "flanagan,owen", "thobhani,ankush", "marikar,umar"})
    @DisplayName("Should Authenticate User")
    void shouldAuthenticateUser(final String username, final String password) {

        User potentialUser = new User(UUID.randomUUID(), username, password, true);

        // Expected
        final Map<HttpStatus, Object> expected = new HashMap<>();
        expected.put(HttpStatus.OK, potentialUser);

        // Given
        // username and password from CSV

        when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(potentialUser));

        // When
        Map<HttpStatus, Object> returnObject = underTest.authenticate(username, password);

        // Then
        assertThat(returnObject).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
            "smith,,Authentication Failed: missing username or password",
            ",james,Authentication Failed: missing username or password",
            "flanagan,,Authentication Failed: missing username or password",
            ",owen,Authentication Failed: missing username or password",
            ",,Authentication Failed: missing username or password",
            // add more checks here
    })
    @DisplayName("Should Not Authenticate User Because Credentials Are Invalid")
    void shouldNotAuthenticateUserBecauseCredentialsAreInvalid(final String username, final String password, final String reason) {

        // Expected
        final Map<HttpStatus, Object> expected = new HashMap<>();
        expected.put(HttpStatus.CONFLICT, reason);

        // Given
        // username and password from CSV

        // When
        Map<HttpStatus, Object> returnObject = underTest.authenticate(username, password);

        // Then
        assertThat(returnObject).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({"smith,james", "flanagan,owen", "thobhani,ankush", "marikar,umar"})
    @DisplayName("Should Not Authenticate User Because User Doesn't Exist")
    void shouldNotAuthenticateUserBecauseUserDoesntExist(final String username, final String password) {

        // Expected
        final Map<HttpStatus, Object> expected = new HashMap<>();
        expected.put(HttpStatus.CONFLICT, String.format("Authentication Failed: user '%s' doesn't exist", username));

        // Given
        // username and password from CSV

        when(userRepository.findByUsername(username))
                .thenReturn(Optional.empty());

        // When
        Map<HttpStatus, Object> returnObject = underTest.authenticate(username, password);

        // Then
        assertThat(returnObject).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({"smith,james", "flanagan,owen", "thobhani,ankush", "marikar,umar"})
    @DisplayName("Should Not Authenticate User Because Password Doesn't Match")
    void shouldNotAuthenticateUserBecausePasswordDoesntMatch(final String username, final String password) {

        User potentialUser = new User(UUID.randomUUID(), username, "f4k3PAssw0rd", true);

        // Expected
        final Map<HttpStatus, Object> expected = new HashMap<>();
        expected.put(HttpStatus.CONFLICT, "Authentication Failed: password is incorrect");

        // Given
        // username and password from CSV

        when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(potentialUser));

        // When
        Map<HttpStatus, Object> returnObject = underTest.authenticate(username, password);

        // Then
        assertThat(returnObject).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({"smith,james", "flanagan,owen", "thobhani,ankush", "marikar,umar"})
    @DisplayName("Should Register User")
    void shouldRegisterUser(final String username, final String password) {

        // Given a potential user
        User potentialUser = new User(UUID.randomUUID(), username, password, true);

        final Map<HttpStatus, Object> expected = new HashMap<>();
        expected.put(HttpStatus.OK, potentialUser);

        // When
        when(userRepository.findByUsername(username))
                .thenReturn(Optional.empty());

        Map<HttpStatus, Object> returnObject = underTest.register(potentialUser);

        // Then
        then(userRepository).should().save(userArgumentCaptor.capture());
        User userArgumentCaptorValue = userArgumentCaptor.getValue();

        assertThat(userArgumentCaptorValue).isEqualTo(potentialUser);
        assertThat(returnObject).isEqualTo(expected);

    }

    @ParameterizedTest
    @CsvSource({"smith,james", "flanagan,owen", "thobhani,ankush", "marikar,umar"})
    @DisplayName("Should Not Register User Because User Already Exists")
    void shouldNotRegisterUserBecauseUserAlreadyExists(final String username, final String password) {

        // Given a potential user
        User potentialUser = new User(UUID.randomUUID(), username, password, true);

        final Map<HttpStatus, Object> expected = new HashMap<>();
        expected.put(HttpStatus.CONFLICT, String.format("Registration Failed: user '%s' already exists", potentialUser.getUsername()));

        // When
        when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(potentialUser));

        Map<HttpStatus, Object> returnObject = underTest.register(potentialUser);

        // Then
        assertThat(returnObject).isEqualTo(expected);

    }

    @ParameterizedTest
    @CsvSource({
            "smith,,Registration Failed: missing username or password",
            ",james,Registration Failed: missing username or password",
            "flanagan,,Registration Failed: missing username or password",
            ",owen,Registration Failed: missing username or password",
            ",,Registration Failed: missing username or password",
            // add more checks here
    })
    @DisplayName("Should Not Register User Because Credentials Are Invalids")
    void shouldNotRegisterUserBecauseCredentialsAreInvalids(final String username, final String password, final String reason) {

        // Given a potential user
        User potentialUser = new User(UUID.randomUUID(), username, password, true);

        final Map<HttpStatus, Object> expected = new HashMap<>();
        expected.put(HttpStatus.CONFLICT, reason);

        // When
        Map<HttpStatus, Object> returnObject = underTest.register(potentialUser);

        // Then
        assertThat(returnObject).isEqualTo(expected);

    }



}