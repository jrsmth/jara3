package com.jrsmiffy.jara3.userservice.service;

import com.jrsmiffy.jara3.userservice.model.AppUser;
import com.jrsmiffy.jara3.userservice.model.UserResponse;

import java.util.List;

public interface UserService {
    // TODO: why use an interface here? - good OOP, decoupled for testing, extensibility?

    UserResponse authenticate(final String username, final String password);

    UserResponse register(final String username, final String password);

    List<AppUser> getAllUsers();
    // TODO: should paginate this response, else performance won't scale as the database grows

}