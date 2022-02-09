package com.jrsmiffy.jara3.userservice.service;

import com.jrsmiffy.jara3.userservice.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);


    public User registerNewUser(User potentialUser){

        log.info("hit me baby one more time");

        return new User();
    }

}
