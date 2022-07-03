package com.jrsmiffy.jara3.userservice.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jrsmiffy.jara3.userservice.model.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private static String JWT_SECRET = "secret";

    @Value("${spring.application.name")
    private static String APP_NAME = "APP_NAME";

    @Value("${jwt.expiration-in-minutes.access}")
    private static int ACCESS_TOKEN_EXPIRATION_MINUTES = 15;

    @Value("${jwt.expiration-in-minutes.refresh}")
    private static int REFRESH_TOKEN_EXPIRATION_MINUTES = 1440;

    /**
     * Signs & creates a JWT for use as an access token
     */
    public String generateAccessToken(String username, Role role) throws IllegalArgumentException, JWTCreationException {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_MINUTES * 60 * 1000))
                .withIssuer(APP_NAME)
                .withClaim("roles", List.of(role.name()))
                .sign(Algorithm.HMAC256(JWT_SECRET));
    }

    /**
     * Signs & creates a JWT for use as an refresh token
     */
    public String generateRefreshToken(String username) throws IllegalArgumentException, JWTCreationException {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_MINUTES * 60 * 1000))
                .withIssuer(APP_NAME)
                .sign(Algorithm.HMAC256(JWT_SECRET));
    }

    /**
     * Verifies token & retrieves the subject
     */
    public String retrieveSubject(String token) throws JWTVerificationException{
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(JWT_SECRET)).build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getSubject();
    }
}
