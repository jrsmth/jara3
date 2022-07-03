package com.jrsmiffy.jara3.userservice.service;

import com.jrsmiffy.jara3.userservice.model.AppUser;
import com.jrsmiffy.jara3.userservice.model.TokenResponse;
import com.jrsmiffy.jara3.userservice.model.UserResponse;
import com.jrsmiffy.jara3.userservice.model.ValidationResponse;
import com.jrsmiffy.jara3.userservice.repository.UserRepository;
import com.jrsmiffy.jara3.userservice.security.jwt.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.apache.logging.log4j.util.Strings.isEmpty;

@Slf4j
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Value("${response.authenticate.success}")
    private String responseAuthenticateSuccess;

    @Value("${response.authenticate.fail.no-user-exists}")
    private String responseAuthenticateFailNoUserExists;

    @Value("${response.authenticate.fail.incorrect-password}")
    private String responseAuthenticateFailIncorrectPassword;

    @Value("${response.authenticate.fail.invalid-credentials}")
    private String responseAuthenticateFailInvalidCredentials;

    @Value("${response.register.success}")
    private String responseRegisterSuccess;

    @Value("${response.register.fail.invalid-credentials}")
    private String responseRegisterFailInvalidCredentials;

    @Value("${response.register.fail.user-exists}")
    private String responseRegisterFailUserExists;

    UserServiceImpl(final UserRepository userRepository, final PasswordEncoder passwordEncoder, final JwtUtils jwtUtils){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        /** NOTE: Constructor injection is preferred over Field injection (@Autowired) */
        /** https://stackoverflow.com/questions/40620000/spring-autowire-on-properties-vs-constructor */
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AppUser> potentialUser = userRepository.findByUsername(username);

        if (potentialUser.isPresent()) {
            log.info("User found in the database: {}", username);
        } else {
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        }

        AppUser user = potentialUser.get();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().toString());
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                List.of(authority));

        return userDetails;
    }
    /** Register */
    @Override
    public UserResponse register(final String username, final String password) {

        // CHECK 0: Are these credentials invalid?
        ValidationResponse validationResponse = validateCredentials(username, password);
        if (validationResponse.isInvalid()) {
            log.error(responseRegisterFailInvalidCredentials + validationResponse.getResponse());
            return new UserResponse(Optional.empty(),
                    responseRegisterFailInvalidCredentials + validationResponse.getResponse());
        }

        String response;
        AppUser registeredUser = null;

        // CHECK 1: Does this user exist in the system?
        if (userRepository.findByUsername(username).isPresent()) {
            response = String.format(responseRegisterFailUserExists, username);
        }
        // CHECKS PASSED: user is registered
        else {
            registeredUser = userRepository.save(new AppUser(username, passwordEncoder.encode(password)));
            response = responseRegisterSuccess;
        }

        log.info(response);
        return new UserResponse(Optional.ofNullable(registeredUser), response);
    }

    /** Get Refreshed Tokens */
    @Override
    public TokenResponse getRefreshedTokens(final String refreshToken) {
        String response = "";
        Map<String, String> tokens = null;

        try {
            String username = jwtUtils.retrieveSubject(refreshToken);
            AppUser user = getUser(username).get();

            String accessToken = jwtUtils.generateAccessToken(username, user.getRole());

            tokens = Map.of(
                    "access_token", accessToken,
                    "refresh_token", refreshToken
            );

            response = "Successfully refreshed tokens";
        } catch (Exception e){
            response = e.getMessage();
        } finally {
            log.info(response);
            return new TokenResponse(Optional.ofNullable(tokens), response);
        }
    }

    /** Get User */
    @Override
    public Optional<AppUser> getUser(final String username) {
        return userRepository.findByUsername(username);
    }

    /** Get All Users */
    @Override
    public List<AppUser> getAllUsers() {
        return userRepository.findAll();
    }

    /** Validate Credentials */
    private ValidationResponse validateCredentials(final String username, final String password) {
        String reason;

        // CHECK 1: are credentials missing username or password?
        if(isEmpty(username) || isEmpty(password)) {
            reason = "missing username or password";
            log.error(reason);
            return new ValidationResponse(false, reason);
        }
        // TODO: add more checks

        reason = "Credentials Validated";
        log.info(reason);
        return new ValidationResponse(true, reason);
    }

}
