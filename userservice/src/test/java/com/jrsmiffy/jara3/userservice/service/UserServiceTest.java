package com.jrsmiffy.jara3.userservice.service;

import com.jrsmiffy.jara3.userservice.model.User;
import com.jrsmiffy.jara3.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
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

    /** authenticate() */
    @ParameterizedTest
    @CsvSource({"kingArthur, r0undT4bl3"})
    @DisplayName("Should Authenticate User")
    void shouldAuthenticateUser(final String username, final String password) {

        // Given: a valid user (that passes the checks)
        User validUser = new User(UUID.randomUUID(), username, password, true);
        Optional<User> expectedResult = Optional.of(validUser);

        // When: mock the repository call & get the result from the method
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(validUser));
        Optional<User> actualResult = underTest.authenticate(username, password);

        // Then: check that this result is equal to the expected
        assertThat(actualResult).isEqualTo(expectedResult);
        verify(underTest).authenticate(username, password);

    }

//    /** authenticate() */
//    @ParameterizedTest
//    @CsvSource({""})
//    @DisplayName("Should Not Authenticate User - Username Doesn't Exist")
//    void shouldNotAuthenticateUserBecauseUsernameDoesntExist(final String username, final String password) {
//
//        // Given
//
//        // When
//
//        // Then
//
//    }
//
//    /** authenticate() */
//    @ParameterizedTest
//    @CsvSource({""})
//    @DisplayName("Should Not Authenticate User - Incorrect Password")
//    void shouldNotAuthenticateUserBecauseIncorrectPassword(final String username, final String password) {
//
//        // Given
//
//        // When
//
//        // Then
//
//    }
//
//    /** authenticate() */
//    @ParameterizedTest
//    @CsvSource({""})
//    @DisplayName("Should Not Authenticate User - Invalid Credentials")
//    void shouldNotAuthenticateUserBecauseInvalidCredentials(final String username, final String password) {
//
//        // Given
//
//        // When
//
//        // Then
//
//    }

    /** register() */




}