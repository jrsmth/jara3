package com.jrsmiffy.jara3.userservice.controller;

import com.jrsmiffy.jara3.userservice.model.AppUser;
import com.jrsmiffy.jara3.userservice.model.TokenResponse;
import com.jrsmiffy.jara3.userservice.model.UserResponse;
import com.jrsmiffy.jara3.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RequiredArgsConstructor // handles our constructor-based dependency injection
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    /** Register User */
    @PostMapping(path = "/register")
    public ResponseEntity<UserResponse> register(@RequestParam final String username,
                                                 @RequestParam final String password) {
        // Check that these credentials are valid: if so, register user + return success; else, return err
        final UserResponse userResponse = userService.register(username, password);

        ResponseEntity<UserResponse> response;

        if(userResponse.getUser().isPresent())
            response = ResponseEntity
                    .status(OK)
                    .body(userResponse);

        else
            response = ResponseEntity
                    .status(BAD_REQUEST)
                    .body(userResponse);

        log.info(response.toString());
        return response;
    }

    /** Refresh Access Token */
    @GetMapping(path = "/token/refresh")
    public ResponseEntity<TokenResponse> refreshAccessToken(@RequestHeader("Authorization") String authorizationHeader) {
        ResponseEntity<TokenResponse> response;

        // Check if authorisation header is valid: if so, try to refresh token; else, return err
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // Check that refresh token is valid: if so, refresh access + return tokens; else, return err
            String refreshToken = authorizationHeader.substring("Bearer ".length());
            final TokenResponse tokenResponse = userService.getRefreshedTokens(refreshToken);

            if (tokenResponse.getTokens().isEmpty())
                response = ResponseEntity
                        .status(FORBIDDEN)
                        .body(tokenResponse);
            else
                response = ResponseEntity
                        .status(OK)
                        .body(tokenResponse);
        } else {
            response = ResponseEntity
                    .status(BAD_REQUEST)
                    .body(new TokenResponse(Optional.empty(), "Refresh token is missing")); // todo: spring profile response?
        }

        log.info(response.toString());
        return response;

    }

    /** Get All Users - Dev Use Only */
    @GetMapping(path = "/admin/users")
    public List<AppUser> getAllUsers() {
        List<AppUser> users = userService.getAllUsers();
        log.info(users.toString());
        return users;
    }
}
