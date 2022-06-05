package com.jrsmiffy.jara3.userservice.controller;

import com.jrsmiffy.jara3.userservice.model.User;
import com.jrsmiffy.jara3.userservice.model.UserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@Slf4j
public class UserController {

    /** Authenticate User */
    @GetMapping(path = "/authenticate/{username}/{password}")
    public ResponseEntity<UserResponse> authenticate(@PathVariable("username") final String username, @PathVariable("password") final String password) {
        log.info("Hello World, from Jara3!");
        User newUser = new User(username, password);
        return ResponseEntity.ok(new UserResponse(Optional.of(newUser), "Hello World, from Jara3!"));
    }

    /** Register User */
    @PostMapping(path = "/register")
    public ResponseEntity<UserResponse> register(@RequestParam final String username, @RequestParam final String password) {
        log.info("Hello World, from Jara3!");
        return ResponseEntity.ok(new UserResponse(Optional.of(new User(username, password)), "Hello World, from Jara3!"));
    }

}
