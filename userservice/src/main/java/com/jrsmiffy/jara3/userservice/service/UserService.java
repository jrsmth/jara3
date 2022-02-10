package com.jrsmiffy.jara3.userservice.service;

import com.jrsmiffy.jara3.userservice.exception.DuplicateUserException;
import com.jrsmiffy.jara3.userservice.exception.InvalidUserCredentialsException;
import com.jrsmiffy.jara3.userservice.model.User;
import com.jrsmiffy.jara3.userservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.apache.commons.lang.StringUtils.isEmpty;

@Service
public class UserService {


    private UserRepository userRepository;

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /** Register New User */
    public Map<HttpStatus, Object> register(final User potentialUser){

        final Map<HttpStatus, Object> returnObject = new HashMap<>();

        try{
            // Does this user already exist?
            if(Math.random() < 0.5)
                throw new DuplicateUserException(String.format("The username '%s' already exists", potentialUser.getUsername()));

            // Save the new user
            // TODO - save user
            returnObject.put(HttpStatus.OK, potentialUser);
        } catch(DuplicateUserException e){
            returnObject.put(HttpStatus.CONFLICT, e.getMessage());
        } finally{
            return returnObject;
        }
    }

    /** Authenticate User */
    public Map<HttpStatus, Object> authenticate(final String username, final String password){

        final Map<HttpStatus, Object> returnObject = new HashMap<>();
        final Optional<User> potentialUser;

        // Reject authentication if user is invalid or if username doesn't exist
        try{
            // Reject authentication if user is invalid
            validateCredentials(username, password);

            // Reject authentication if username doesn't exist
            potentialUser = userRepository.findByUsername(username);
            if(potentialUser.equals(Optional.empty())){
                throw new InvalidUserCredentialsException(String.format("Authentication Failed: user '%s' doesn't exist", username));
            }

            // Reject authentication if password is incorrect
            if(!password.equals(potentialUser.get().getPassword())){
                throw new InvalidUserCredentialsException("Authentication Failed: password is incorrect");
            }

        } catch(InvalidUserCredentialsException e){
            returnObject.put(HttpStatus.CONFLICT, e.getMessage());
            log.info(e.getMessage());
            return returnObject;
        }

        returnObject.put(HttpStatus.OK, potentialUser.get());
        log.info("Authentication Successful: " + username);

        return returnObject;
    }

    /** Validate Credentials */
    private void validateCredentials(String username, String password){

        // User is invalid if it missing a username or password
        if(isEmpty(username) | isEmpty(password))
            throw new InvalidUserCredentialsException("Authentication Failed: missing username or password");

        // more checks required

    }

}
