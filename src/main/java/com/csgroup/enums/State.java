package com.csgroup.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum State {

    STARTED, FINISHED;

    @JsonValue
    public String toValue() {
        return name();
    }

}
