package com.jrsmiffy.jara3.userservice.service;

import com.jrsmiffy.jara3.userservice.model.AppUser;
import com.jrsmiffy.jara3.userservice.model.UserResponse;

import java.util.List;
import java.util.Optional;

public interface UserService {
    // TODO: why use an interface here? - good OOP, decoupled for testing, extensibility?

    UserResponse authenticate(final String username, final String password);

    UserResponse register(final String username, final String password);

    List<AppUser> getAllUsers();
    // TODO: should paginate this response, else performance won't scale as the database grows

    Optional<AppUser> getUser(String username); // todo: refactor this sh!te, inconsistent use of user/app user...

    // TODO: complete miro designs for user svc / auth story...

    // TODO NEXT STEPS
    // todo: take on unit tests for controller, refactor as you go...
    // todo: see todos in UserIT

    // SVC FUNCTIONALITY:
        // POST to /login with {username, password}
            // response: 200 OK with tokens or 401 UNAUTHORISED with "Bad Credentials" only
        // POST to /register {username, password}
            // decision: add an admin user by default to seed the database, let all other users by created with USER
                // response with current set up more or less... user plus the response msg...
        // GET To refresh token (by anyone)
        // GET to get all users (by admins only) - 403 FORBIDDEN



}


// the 401 for unauthenticated users, the 403 for authenticated users with insufficient permissions
// https://stackoverflow.com/questions/3297048/403-forbidden-vs-401-unauthorized-http-responses
// Authorisation Filter: FORBIDDEN 403
// AUTHENTICATED Filter: UNAUTHORISED 401 (should really be UNAUTHENTICATED... as per stack overflow)