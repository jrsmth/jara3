package com.jrsmiffy.jara3.userservice.controller;

import com.jrsmiffy.jara3.userservice.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

//    /** Register New User */
//    @RequestMapping(path = "/register", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
//    public @ResponseBody ResponseEntity<Map<HttpStatus, Object>> register(@RequestBody final User potentialUser) {
//
//        // Check that this potential user is valid - if so, register them; else, return exception
//        final Map<HttpStatus, Object> returnObject = userService.register(potentialUser);
//
//        return ResponseEntity.ok(returnObject);
//    }
//
//
//    /** Authenticate User */
//    @RequestMapping(path = "/authenticate/{username}/{password}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//    public @ResponseBody ResponseEntity<Map<HttpStatus, Object>> authenticate(@PathVariable("username") final String username, @PathVariable("password") final String password) {
//
//        // Check that these user credentials match with a valid user - if so, return success; else, return err
//        final Map<HttpStatus, Object> returnObject = userService.authenticate(username, password);
//
//        return ResponseEntity.ok(returnObject);
//
//    }
//
//    /** Get All Users - Dev Purposes */
//    @RequestMapping(path = "/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//    public @ResponseBody
//    ResponseEntity<Object> getAllUsers() {
//        return ResponseEntity.ok(userService.getAllUsers());
//    }


    /** Get All Users - Dev Purposes */
    @RequestMapping(path = "/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseEntity<Object> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }





}