package com.jrsmiffy.jara3.userservice.controller;

import com.jrsmiffy.jara3.userservice.model.UserResponse;
import com.jrsmiffy.jara3.userservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class UserController {

    @Autowired
    UserService userService;

    /** Authenticate User */
    @GetMapping(path = "/authenticate/{username}/{password}")
    public ResponseEntity<UserResponse> authenticate(@PathVariable("username") final String username,
                                                     @PathVariable("password") final String password) {
        // Check that these credentials are valid: if so, return success; else, return err
        final UserResponse userResponse = userService.authenticate(username, password);
        return createResponseEntity(userResponse);
    }

    /** Register User */
    @PostMapping(path = "/register")
    public ResponseEntity<UserResponse> register(@RequestParam final String username,
                                                 @RequestParam final String password) {
        // Check that these credentials are valid: if so, register user + return success; else, return err
        final UserResponse userResponse = userService.register(username, password);
        return createResponseEntity(userResponse);
    }

    /** Create Response Entity*/
    private ResponseEntity<UserResponse> createResponseEntity(final UserResponse userResponse) {
        // Create response entity, based on whether User is null or not

        ResponseEntity<UserResponse> response;

        if(userResponse.getUser().isPresent())
            response = ResponseEntity.status(HttpStatus.OK)
                    .body(userResponse);

        else
            response = ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(userResponse);

        log.info(response.toString());
        return response;
    }


    
    /** Add and test the dev endpoint and then move onto User svc TDD */

}
