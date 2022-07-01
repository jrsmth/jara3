package com.jrsmiffy.jara3.userservice.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jrsmiffy.jara3.userservice.model.AppUser;
import com.jrsmiffy.jara3.userservice.model.UserResponse;
import com.jrsmiffy.jara3.userservice.security.jwt.JwtUtils;
import com.jrsmiffy.jara3.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor // handles our constructor-based dependency injection
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    private final JwtUtils jwtUtils;

    @GetMapping(path = "/token/refresh") // todo: should be in a separate controller, or leave here? - Token/SecurityController?
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refreshToken = authorizationHeader.substring("Bearer ".length());
                log.info("hit -3");
                String username = jwtUtils.retrieveSubject(refreshToken);
                log.info("hit -2");
                AppUser user = userService.getUser(username).get(); // test leakage? runs on its own....
                log.info("hit -1");
                log.info(user.getRole().toString());
                String accessToken = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 15 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", List.of(user.getRole().name()))
                        .sign(Algorithm.HMAC256("secret"));

                log.info("hit 0");

                Map<String, String> tokens = Map.of("access_token", accessToken, "refresh_token", refreshToken);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (Exception e) { // TODO: This is used for invalid token (1/3 of parts, etc..., not for lesser authority - enhance?)
                log.info("hit");
                log.error("Error: " + e.toString());
                response.setHeader("error", e.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = Map.of("error_message", e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE); // redundant? @RestController
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Refresh Token is missing");
        }
    }


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

    /** Get All Users - Dev Use Only */
    @GetMapping(path = "/admin/users")
    public List<AppUser> getAllUsers() {
        List<AppUser> users = userService.getAllUsers();
        log.info(users.toString());
        return users;
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

}
