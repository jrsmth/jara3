package com.jrsmiffy.jara3.userservice;

import com.jrsmiffy.jara3.userservice.model.User;
import com.jrsmiffy.jara3.userservice.repository.UserRepository;
import com.jrsmiffy.jara3.userservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService underTest;

    @BeforeEach
    void setUp() {
        underTest = new UserService(userRepository);
    }

    @ParameterizedTest
    @CsvSource({"smith,james", "flanagan,owen", "thobhani,ankush", "marikar,umar"})
    @DisplayName("Should Authenticate User")
    void shouldAuthenticateUser(String username, String password) {

        User potentialUser = new User(0L, username, password, true);

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
    void shouldNotAuthenticateUserBecauseCredentialsAreInvalid(String username, String password, String reason) {

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
    void shouldNotAuthenticateUserBecauseUserDoesntExist(String username, String password) {

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
    void shouldNotAuthenticateUserBecausePasswordDoesntMatch(String username, String password) {

        User potentialUser = new User(0L, username, "f4k3PAssw0rd", true);

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



}