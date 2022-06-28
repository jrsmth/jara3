package com.jrsmiffy.jara3.userservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ErrorInfo {
    @JsonIgnore
    public final StringBuffer url;
    public final String ex;

    public ErrorInfo(StringBuffer stringBuffer, Exception ex) {
        this.url = stringBuffer;
        this.ex = ex.getLocalizedMessage();
    }
}