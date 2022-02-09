package com.jrsmiffy.jara3.userservice.service;

import com.jrsmiffy.jara3.userservice.exception.DuplicateUserException;
import com.jrsmiffy.jara3.userservice.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);


    public Map<HttpStatus, Object> registerNewUser(User potentialUser){

        log.info("hit me baby one more time");
        Map<HttpStatus, Object> returnObject = new HashMap<>();

        try{
            // Does this user already exist?
            if(Math.random() < 0.5)
                throw new DuplicateUserException(String.format("The username '%s' already exists", potentialUser.getUsername()));

            // Save the new user
            // TODO - save user
            returnObject.put(HttpStatus.OK, potentialUser);
        } catch(DuplicateUserException e){
            returnObject.put(HttpStatus.CONFLICT, e.getMessage());
        } finally{
            return returnObject;
        }
    }

}
