package com.jrsmiffy.jara3.userservice.security;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    // Spring Boot <2.7.0 is required to use WebSecurityConfigurerAdapter:
    // https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter


}
