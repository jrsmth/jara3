package com.jrsmiffy.jara3.userservice.service;

import com.jrsmiffy.jara3.userservice.model.User;
import com.jrsmiffy.jara3.userservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /** Register New User */
    public Map<HttpStatus, Object> register(final User potentialUser){
        return null;
    }

    /** Authenticate User */
    public Optional<User> authenticate(final String username, final String password){

        // perform checks on the username, password and user
        Optional<User> thisUser = userRepository.findByUsername(username);

        // return Optional.empty() if any check fails

        // return User if all checks pass
        return thisUser;
    }

    /** Validate Credentials */
    private Boolean validateCredentials(final String username, final String password){
        return true;
    }

    /** Get All Users */
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }


}
