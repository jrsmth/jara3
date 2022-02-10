package com.jrsmiffy.jara3.userservice.repository;

import com.jrsmiffy.jara3.userservice.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUsername(String username);

}
