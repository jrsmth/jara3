package com.jrsmiffy.jara3.userservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class ValidationResponse {

    private Boolean valid;

    @Getter
    private String response;

    public Boolean isInvalid() {
        return !valid;
    }
}
