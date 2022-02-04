package com.jrsmiffy.jara3.userservice.model;

import lombok.Data;
import javax.persistence.*;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(unique = true)
    private String username;

    private String password;

    private boolean active = Boolean.TRUE;

}