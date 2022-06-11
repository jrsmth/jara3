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

    UserService(final UserRepository userRepository){
        this.userRepository = userRepository;
        /** NOTE: Constructor injection is preferred over Field injection (@Autowired) */
        /** https://stackoverflow.com/questions/40620000/spring-autowire-on-properties-vs-constructor */
    }

    /** Authenticate */
    public UserResponse authenticate(final String username, final String password) {
        Optional<User> potentialUser = userRepository.findByUsername(username);
        return new UserResponse(potentialUser, responseAuthenticateSuccess);
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
}
