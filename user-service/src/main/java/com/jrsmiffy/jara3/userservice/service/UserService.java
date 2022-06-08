package com.jrsmiffy.jara3.userservice.service;

import com.jrsmiffy.jara3.userservice.model.User;
import com.jrsmiffy.jara3.userservice.model.UserResponse;
import com.jrsmiffy.jara3.userservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public UserResponse authenticate(final String username, final String password) {
        User potentialUser = userRepository.findByUsername(username).get();
        log.info(new UserResponse(Optional.of(potentialUser), "Hello World, from Jara3!").toString());
        return new UserResponse(Optional.of(potentialUser), "Hello World, from Jara3!");
    }

    public UserResponse register(final String username, final String password){
        User newUser = userRepository.save(new User(username, password));
        return new UserResponse(Optional.of(newUser), "Hello World, from Jara3!");
    }

}
