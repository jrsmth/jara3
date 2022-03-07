package com.jrsmiffy.jara3.userservice.service;

import com.jrsmiffy.jara3.userservice.model.User;
import com.jrsmiffy.jara3.userservice.model.UserResponse;
import com.jrsmiffy.jara3.userservice.model.ValidationResponse;
import com.jrsmiffy.jara3.userservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.apache.logging.log4j.util.Strings.isEmpty;

@Service
public class UserService {

    private UserRepository userRepository;

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /** Register User */
    public UserResponse register(final User potentialUser){

        // perform checks on the username and password
            // return Optional.empty() and response if any check fails
            // save the User and return it if all checks pass

        log.info("*** Registration Checks ***");
        log.info("Username: " + potentialUser.getUsername());
        log.info("Password: " + potentialUser.getPassword());
        String response;

        // check 1: are these credentials invalid?
        ValidationResponse validationResponse = validateCredentials(potentialUser.getUsername(), potentialUser.getPassword());
        if(validationResponse.isInvalid()){
            response = "Registration Failed: " + validationResponse.getReason();
            log.info(response);
            return new UserResponse(Optional.empty(), response);
        }

        // check 2: does this user exist already?
        if(userRepository.findByUsername(potentialUser.getUsername()).isPresent()){
            response = String.format("Registration Failed: username '%s' already exists", potentialUser.getUsername());
            log.info(response);
            return new UserResponse(Optional.empty(), response);
        }

        userRepository.save(potentialUser);
        response = "Registration Successful";
        log.info(response);
        return new UserResponse(Optional.of(potentialUser), response);
    }

    /** Authenticate User */
    public UserResponse authenticate(final String username, final String password){

        // perform checks on the username and password
            // return Optional.empty() and response if any check fails
                // return {User, response} if all checks pass

        log.info("*** Authentication Checks ***");
        log.info("Username: " + username);
        log.info("Password: " + password);
        String response;

        // check 1: are these credentials invalid?
        ValidationResponse validationResponse = validateCredentials(username, password);
        if(validationResponse.isInvalid()){
            response = "Authentication Failed: " + validationResponse.getReason();
            log.info(response);
            return new UserResponse(Optional.empty(), response);
        }

        // check 2: does this user exist?
        Optional<User> thisUser = userRepository.findByUsername(username);
        if(thisUser.isEmpty()){
            response = String.format("Authentication Failed: user '%s' does not exist", username);
            log.info(response);
            return new UserResponse(Optional.empty(), response);
        }

        // check 3: does the password match?
        if(!thisUser.get().getPassword().equals(password)){
            response = "Authentication Failed: password doesn't match";
            log.info(response);
            return new UserResponse(Optional.empty(), response);
        }

        response = "Authentication Successful";
        log.info(response);
        return new UserResponse(thisUser, response);
    }

    /** Validate Credentials */
    private ValidationResponse validateCredentials(final String username, final String password){

        // perform conditional checks on the username and password
            // return false and reason if any check fails
            // return true and reason if all checks pass

        String reason;

        if(isEmpty(username) || isEmpty(password)){ // check 1: missing username or password
            reason = "missing username or password";
            log.info(reason);
            return new ValidationResponse(false, reason);
        } // TODO: add more checks

        reason = "Credentials Validated";
        log.info(reason);
        return new ValidationResponse(true, reason);

    }

    /** Get All Users */
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

}
