package com.jrsmiffy.jara3.userservice.repository;

import com.jrsmiffy.jara3.userservice.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);

}
