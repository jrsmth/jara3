package com.jrsmiffy.jara3.userservice.controller;

import com.netflix.discovery.EurekaClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    /**
     * Adding Client Service Discovery Capabilities
     */
    @Autowired
    @Lazy
    private EurekaClient eurekaClient;

    @Value("${spring.application.name}")
    private String appName;

    /**
     * Retrieve User by names - login
     */
    @RequestMapping(path = "/login", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<Map<HttpStatus, Object>> login(@RequestParam("user") String user, @RequestParam("pass") String pass) {

        log.info("hit me baby");

        Map<HttpStatus, Object> returnObject = new HashMap<>();
        returnObject.put(HttpStatus.OK, "Connection confirmed");
        return ResponseEntity.ok(returnObject);
    }





}



