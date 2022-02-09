package com.jrsmiffy.jara3.userservice.controller;

import com.jrsmiffy.jara3.userservice.model.User;
import com.jrsmiffy.jara3.userservice.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /** Register A New User */
    @RequestMapping(path = "/user", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<Map<HttpStatus, Object>> registerUser(@RequestBody User potentialUser) {

        // Validate that this user is valid - if so, register them
        User registeredUser = userService.registerNewUser(potentialUser);

        // Response
        Map<HttpStatus, Object> returnObject = new HashMap<>();
        returnObject.put(HttpStatus.OK, registeredUser);

        return ResponseEntity.ok(returnObject);
    }















//    /**
//     * Login - Verify User Credentials & Accept/Reject Login Request
//     */
//        @RequestMapping(path = "/login/{username}/{password}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//        public @ResponseBody ResponseEntity<Map<HttpStatus, Object>> login(@PathVariable("username") String username, @PathVariable("password") String password) {
//            Map<HttpStatus, Object> returnObject = new HashMap<>();
//            returnObject.put(HttpStatus.OK, "Connection confirmed");
//            return ResponseEntity.ok(returnObject);
//        }
//
//        /**
//         * Create User
//         */
//        @RequestMapping(path = "/user", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
//        public @ResponseBody
//        ResponseEntity<Map<HttpStatus, Object>> createUser(@RequestBody User newUser) {
//
//            // Are these credentials valid? -> they have already been validated in the frontend
//
//            Map<HttpStatus, Object> returnObject = new HashMap<>();
//            try{
//                // Does this user already exist?
//                userRepository.findByUsername(newUser.getUsername()); // wtf is this logic...
//                userRepository.save(newUser);
//            } catch (NullPointerException e){
//                log.error(e.toString());
//            } finally {
//                returnObject.put(HttpStatus.OK, newUser);
//                return ResponseEntity.ok(returnObject);
//            }
//    }





}