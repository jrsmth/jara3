package com.jrsmiffy.jara3.userservice.controller;

import com.jrsmiffy.jara3.userservice.model.User;
import com.jrsmiffy.jara3.userservice.repository.UserRepository;
import com.netflix.discovery.EurekaClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    /**
     * Adding Client Service Discovery Capabilities
     */
    @Autowired
    @Lazy
    private EurekaClient eurekaClient;

    @Value("${spring.application.name}")
    private String appName;

    @Autowired
    private UserRepository userRepository;

    /**
     * Login - Verify User Credentials & Accept/Reject Login Request
     */
    @RequestMapping(path = "/login/{username}/{password}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<Map<HttpStatus, Object>> login(@PathVariable("username") String username, @PathVariable("password") String password) {
        Map<HttpStatus, Object> returnObject = new HashMap<>();
        returnObject.put(HttpStatus.OK, "Connection confirmed");
        return ResponseEntity.ok(returnObject);
    }

    /**
     * Create User
     */
    @RequestMapping(path = "/user", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<Map<HttpStatus, Object>> createUser(@RequestBody User newUser) {

        // Are these credentials valid? -> they have already been validated in the frontend

        Map<HttpStatus, Object> returnObject = new HashMap<>();
        try{
            // Does this user already exist?
            userRepository.findByUsername(newUser.getUsername());
            userRepository.save(newUser);
        } catch (NullPointerException e){
            log.error(e.toString());
        } finally {
            returnObject.put(HttpStatus.OK, newUser);
            return ResponseEntity.ok(returnObject);
        }
    }





}