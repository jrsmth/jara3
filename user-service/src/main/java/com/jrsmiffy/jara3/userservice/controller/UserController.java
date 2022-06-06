package com.jrsmiffy.jara3.userservice.controller;

import com.jrsmiffy.jara3.userservice.model.UserResponse;
import com.jrsmiffy.jara3.userservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class UserController {

    @Autowired
    UserService userService;

    /** Authenticate User */
    @GetMapping(path = "/authenticate/{username}/{password}")
    public ResponseEntity<UserResponse> authenticate(@PathVariable("username") final String username, @PathVariable("password") final String password) {
        log.info("Hello World, from Jara3!");
        UserResponse userResponse = userService.authenticate(username, password);
        return ResponseEntity.ok(userResponse);
    }

    /** Register User */
    @PostMapping(path = "/register")
    public ResponseEntity<UserResponse> register(@RequestParam final String username, @RequestParam final String password) {
        log.info("Hello World, from Jara3!");
        UserResponse userResponse = userService.register(username, password);
        return ResponseEntity.ok(userResponse);
    }

}
