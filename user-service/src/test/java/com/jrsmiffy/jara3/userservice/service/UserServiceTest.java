package com.jrsmiffy.jara3.userservice.service;

import com.jrsmiffy.jara3.userservice.model.User;
import com.jrsmiffy.jara3.userservice.model.UserResponse;
import com.jrsmiffy.jara3.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest // required to load spring profile (app.yaml)
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class UserServiceTest {

    @InjectMocks
    private UserService underTest;

    @Mock
    private UserRepository mockRepository;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Value("${response.authenticate.success}")
    private String responseAuthenticateSuccess;

    @Value("${response.authenticate.fail.no-user-exists}")
    private String responseAuthenticateFailNoUserExists;

    @Value("${response.authenticate.fail.incorrect-password}")
    private String responseAuthenticateFailIncorrectPassword;

    @Value("${response.authenticate.fail.invalid-credentials}")
    private String responseAuthenticateFailInvalidCredentials;

    @Value("${response.register.success}")
    private String responseRegisterSuccess;

    @Value("${response.register.fail.user-exists}")
    private String responseRegisterFailUserExists;

    @Value("${response.register.fail.invalid-credentials}")
    private String responseRegisterFailInvalidCredentials;

    @BeforeEach // @BeforeEach vs @Before, former is necessary for ReflectionTestUtils.setField() [at least...]
    void setup() {
        this.underTest = new UserService(mockRepository);
    }

    @ParameterizedTest
    @CsvFileSource(resources="/users.csv")
    @DisplayName("Should Authenticate User")
    void shouldAuthenticateUser(final String username, final String password){
        // Given: a valid user (that passes the checks)
        User validUser = new User(UUID.randomUUID(), username, password, true);
        UserResponse expected = new UserResponse(Optional.of(validUser), responseAuthenticateSuccess);
        ReflectionTestUtils.setField(underTest, "responseAuthenticateSuccess", responseAuthenticateSuccess);

        // When:
        when(mockRepository.findByUsername(username)).thenReturn(Optional.of(validUser));
        final UserResponse actual = underTest.authenticate(username, password);

        // Then:
        assertThat(actual).isEqualTo(expected);

        verify(mockRepository).findByUsername(username);
    }

    @ParameterizedTest
    @CsvFileSource(resources="/users.csv")
    @DisplayName("Should Not Authenticate User Because Username Does Not Exist")
    void shouldNotAuthenticateUserBecauseUsernameDoesNotExist(final String username, final String password) {
        // Given: a username that does not exist in the system
        UserResponse expected = new UserResponse(Optional.empty(), String.format(responseAuthenticateFailNoUserExists, username));
        ReflectionTestUtils.setField(underTest, "responseAuthenticateFailNoUserExists", responseAuthenticateFailNoUserExists);

        // When:
        when(mockRepository.findByUsername(username)).thenReturn(Optional.empty());
        final UserResponse actual = underTest.authenticate(username, password);

        // Then:
        assertThat(actual).isEqualTo(expected);

        verify(mockRepository).findByUsername(username);
    }

    @ParameterizedTest
    @CsvFileSource(resources="/users.csv")
    @DisplayName("Should Not Authenticate User Because Password Does Not Match")
    void shouldNotAuthenticateUserBecausePasswordDoesNotMatch(final String username, final String password) {
        // Given: a password that does not match the password in the system for this username
        final User savedUser = new User(UUID.randomUUID(), username, "INCORRECT_PASSWORD", true);
        UserResponse expected = new UserResponse(Optional.empty(), responseAuthenticateFailIncorrectPassword);
        ReflectionTestUtils.setField(underTest, "responseAuthenticateFailIncorrectPassword", responseAuthenticateFailIncorrectPassword);

        // When:
        when(mockRepository.findByUsername(username)).thenReturn(Optional.of(savedUser));
        final UserResponse actual = underTest.authenticate(username, password);

        // Then:
        assertThat(actual).isEqualTo(expected);

        verify(mockRepository).findByUsername(username);
    }

    @ParameterizedTest
    @CsvFileSource(resources="/users_invalid.csv")
    @DisplayName("Should Not Authenticate User Because Credentials Are Invalid")
    void shouldNotAuthenticateUserBecauseCredentialsAreInvalid(final String username, final String password, final String reason) {
        // Given: credentials (username and/or password) that are invalid
        UserResponse expected = new UserResponse(Optional.empty(), responseAuthenticateFailInvalidCredentials + reason);
        ReflectionTestUtils.setField(underTest, "responseAuthenticateFailInvalidCredentials", responseAuthenticateFailInvalidCredentials);

        // When:
        final UserResponse actual = underTest.authenticate(username, password);

        // Then:
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvFileSource(resources="/users.csv")
    @DisplayName("Should Register User")
    void shouldRegisterUser(final String username, final String password) {
        // Given: a valid user (that passes the checks)
        User validUser = new User(UUID.randomUUID(), username, password, true);
        UserResponse expected = new UserResponse(Optional.of(validUser), responseRegisterSuccess);
        ReflectionTestUtils.setField(underTest, "responseRegisterSuccess", responseRegisterSuccess);

        // When:
        when(mockRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(mockRepository.save(new User(username, password))).thenReturn(validUser);
        final UserResponse actual = underTest.register(username, password);

        // Then:
        assertThat(actual).isEqualTo(expected);

        verify(mockRepository).findByUsername(username);
        verify(mockRepository).save(new User(username, password));
    }

    @ParameterizedTest
    @CsvFileSource(resources="/users.csv")
    @DisplayName("Should Not Register User Because Username Exists")
    void shouldNotRegisterUserBecauseUsernameExists(final String username, final String password) {
        // Given: a username that already exists in the system
        UserResponse expected = new UserResponse(Optional.empty(), String.format(responseRegisterFailUserExists, username));
        ReflectionTestUtils.setField(underTest, "responseRegisterFailUserExists", responseRegisterFailUserExists);

        // When:
        when(mockRepository.findByUsername(username)).thenReturn(Optional.of(new User(username, password)));
        final UserResponse actual = underTest.register(username, password);

        // Then:
        assertThat(actual).isEqualTo(expected);

        verify(mockRepository).findByUsername(username);
    }

    @ParameterizedTest
    @CsvFileSource(resources="/users_invalid.csv")
    @DisplayName("Should Not Register User Because Credentials Are Invalid")
    void shouldNotRegisterUserBecauseCredentialsAreInvalid(final String username, final String password, final String reason) {
        // Given: credentials (username and/or password) that are invalid
        UserResponse expected = new UserResponse(Optional.empty(), responseRegisterFailInvalidCredentials + reason);
        ReflectionTestUtils.setField(underTest, "responseRegisterFailInvalidCredentials", responseRegisterFailInvalidCredentials);

        // When:
        final UserResponse actual = underTest.register(username, password);

        // Then:
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/users.csv")
    @DisplayName("Should Get All Users")
    void shouldGetAllUsers(final String username, final String password) {
        User savedUser = new User(UUID.randomUUID(), username, password, true);

        // When
        when(mockRepository.findAll())
                .thenReturn(List.of(savedUser));

        // Then
        assertThat(underTest.getAllUsers()).isEqualTo(List.of(savedUser));

        verify(mockRepository).findAll();
    }

}
