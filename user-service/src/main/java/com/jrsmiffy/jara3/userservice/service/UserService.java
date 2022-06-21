package com.jrsmiffy.jara3.userservice.service;

import com.jrsmiffy.jara3.userservice.model.User;
import com.jrsmiffy.jara3.userservice.model.UserResponse;
import com.jrsmiffy.jara3.userservice.model.ValidationResponse;
import com.jrsmiffy.jara3.userservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.apache.logging.log4j.util.Strings.isEmpty;

@Slf4j
@Service
public class UserService {

    private UserRepository userRepository;

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

    @Value("${response.register.fail.invalid-credentials}")
    private String responseRegisterFailInvalidCredentials;

    @Value("${response.register.fail.user-exists}")
    private String responseRegisterFailUserExists;

    UserService(final UserRepository userRepository){
        this.userRepository = userRepository;
        /** NOTE: Constructor injection is preferred over Field injection (@Autowired) */
        /** https://stackoverflow.com/questions/40620000/spring-autowire-on-properties-vs-constructor */
    }

    /** Authenticate */
    public UserResponse authenticate(final String username, final String password) {

        // CHECK 0: Are these credentials invalid?
        ValidationResponse validationResponse = validateCredentials(username, password);
        if (validationResponse.isInvalid()) {
            log.info(responseAuthenticateFailInvalidCredentials + validationResponse.getResponse());
            return new UserResponse(Optional.empty(),
                    responseAuthenticateFailInvalidCredentials + validationResponse.getResponse());
        }

        String response;
        Optional<User> potentialUser = userRepository.findByUsername(username);

        // CHECK 1: Does this user not exist in the system?
        if (potentialUser.isEmpty()) {
            response = String.format(responseAuthenticateFailNoUserExists, username);
        }
        // CHECK 2: Does the password not match?
        else if (!potentialUser.get().getPassword().equals(password)) {
            response = responseAuthenticateFailIncorrectPassword;
            potentialUser = Optional.empty();
        }
        // CHECKS PASSED: user is authenticated
        else {
            response = responseAuthenticateSuccess;
        }

        log.info(response);
        return new UserResponse(potentialUser, response);
    }

    /** Register */
    public UserResponse register(final String username, final String password) {

        // CHECK 0: Are these credentials invalid?
        ValidationResponse validationResponse = validateCredentials(username, password);
        if (validationResponse.isInvalid()) {
            log.info(responseRegisterFailInvalidCredentials + validationResponse.getResponse());
            return new UserResponse(Optional.empty(),
                    responseRegisterFailInvalidCredentials + validationResponse.getResponse());
        }

        String response;
        User registeredUser = null;

        // CHECK 1: Does this user exist in the system?
        if (userRepository.findByUsername(username).isPresent()) {
            response = String.format(responseRegisterFailUserExists, username);
        }
        // CHECKS PASSED: user is registered
        else {
            registeredUser = userRepository.save(new User(username, password));
            response = responseRegisterSuccess;
        }

        log.info(response);
        return new UserResponse(Optional.ofNullable(registeredUser), response);
    }

    /** Get All Users */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /** Validate Credentials */
    private ValidationResponse validateCredentials(final String username, final String password) {
        String reason;

        // CHECK 1: are credentials missing username or password?
        if(isEmpty(username) || isEmpty(password)) {
            reason = "missing username or password";
            log.info(reason);
            return new ValidationResponse(false, reason);
        }
        // TODO: add more checks

        reason = "Credentials Validated";
        log.info(reason);
        return new ValidationResponse(true, reason);
    }

    
    // TODO: implement JWT, then done for this service? (delete legacy)
        // get the DB running and test it manually in postman...

    //  merge branch + start proper branching and using JIRA
        // TODO: redo frontend and add e2e tests support for JWT (frontend)
}
