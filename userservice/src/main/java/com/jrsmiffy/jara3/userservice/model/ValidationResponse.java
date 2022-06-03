package com.jrsmiffy.jara3.userservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationResponse {

    private Boolean valid;
    private String reason;

    public Boolean isInvalid() {
        return !valid;
    }
}
