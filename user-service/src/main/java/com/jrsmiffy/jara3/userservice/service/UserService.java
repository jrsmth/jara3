package com.jrsmiffy.jara3.userservice.service;

import com.jrsmiffy.jara3.userservice.model.User;
import com.jrsmiffy.jara3.userservice.model.UserResponse;
import com.jrsmiffy.jara3.userservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    UserService(final UserRepository userRepository){
        this.userRepository = userRepository;
        /** NOTE: Constructor injection is preferred over Field injection (@Autowired) */
        /** https://stackoverflow.com/questions/40620000/spring-autowire-on-properties-vs-constructor */
    }

    /** Authenticate */
    public UserResponse authenticate(final String username, final String password) {
        Optional<User> potentialUser = userRepository.findByUsername(username);
        String response;

        // TODO: add logging

        // CHECK 1: Does this user exist in the system?
        if (potentialUser.isEmpty()) {
            response = String.format(responseAuthenticateFailNoUserExists, username);
        }
        // CHECK 2: Does the password match?
        else if (!potentialUser.get().getPassword().equals(password)) {
            response = String.format(responseAuthenticateFailIncorrectPassword);
            potentialUser = Optional.empty();
        }
        // CHECKS PASSED
        else {
            response = responseAuthenticateSuccess;
        }

        return new UserResponse(potentialUser, response);
    }

    /** Register */
    public UserResponse register(final String username, final String password){
        User newUser = userRepository.save(new User(username, password));
        return new UserResponse(Optional.of(newUser), "Hello World, from Jara3!");
    }

    /** Get All Users */
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    // TODO: UserService TDD
    // TODO: implement JWT, then done for this service? (delete legacy)
    //  merge branch + start proper branching and using JIRA
        // TODO: redo frontend and add e2e tests support for JWT (frontend)
}
