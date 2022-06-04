package com.jrsmiffy.jara3.userservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class UserController {

    /** Authenticate User */
    @GetMapping(path = "/authenticate/{username}/{password}")
    public ResponseEntity<Object> authenticate(@PathVariable("username") final String username, @PathVariable("password") final String password) {
        log.info("Hello World, from Jara3!");
        return ResponseEntity.ok("Hello World, from Jara3!");
    }

}
