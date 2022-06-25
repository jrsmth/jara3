package com.jrsmiffy.jara3.userservice.service;

import com.jrsmiffy.jara3.userservice.model.AppUser;
import com.jrsmiffy.jara3.userservice.model.UserResponse;
import com.jrsmiffy.jara3.userservice.model.ValidationResponse;
import com.jrsmiffy.jara3.userservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.apache.logging.log4j.util.Strings.isEmpty;

@Slf4j
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

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

    UserServiceImpl(final UserRepository userRepository, final PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

    // TODO: override from interface?
    // TODO: make "AppUser appUser" consistent through the whole app? now we have inconsistent naming throughout the svc, think + refactor...
    public AppUser saveUser(AppUser appUser) {
        log.info("Saving new user to the databse: {}", appUser.getUsername());
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        return userRepository.save(appUser);
    }

    /** Authenticate */
    @Override
    public UserResponse authenticate(final String username, final String password) {

        // CHECK 0: Are these credentials invalid?
        ValidationResponse validationResponse = validateCredentials(username, password);
        if (validationResponse.isInvalid()) {
            log.info(responseAuthenticateFailInvalidCredentials + validationResponse.getResponse());
            return new UserResponse(Optional.empty(),
                    responseAuthenticateFailInvalidCredentials + validationResponse.getResponse());
        }

        String response;
        Optional<AppUser> potentialUser = userRepository.findByUsername(username);

        // CHECK 1: Does this user not exist in the system?
        if (potentialUser.isEmpty()) {
            response = String.format(responseAuthenticateFailNoUserExists, username);
        }
        // CHECK 2: Does the password not match?
        else if (!potentialUser.get().getPassword().equals(password)) {
            response = responseAuthenticateFailIncorrectPassword;
            potentialUser = Optional.empty();
        }
        // CHECKS PASSED: user is authenticated
        else {
            response = responseAuthenticateSuccess;
        }

        log.info(response);
        return new UserResponse(potentialUser, response);
    }

    /** Register */
    @Override
    public UserResponse register(final String username, final String password) {

        // CHECK 0: Are these credentials invalid?
        ValidationResponse validationResponse = validateCredentials(username, password);
        if (validationResponse.isInvalid()) {
            log.info(responseRegisterFailInvalidCredentials + validationResponse.getResponse());
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
            registeredUser = userRepository.save(new AppUser(username, password));
            response = responseRegisterSuccess;
        }

        log.info(response);
        return new UserResponse(Optional.ofNullable(registeredUser), response);
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
            log.info(reason);
            return new ValidationResponse(false, reason);
        }
        // TODO: add more checks

        reason = "Credentials Validated";
        log.info(reason);
        return new ValidationResponse(true, reason);
    }

}
