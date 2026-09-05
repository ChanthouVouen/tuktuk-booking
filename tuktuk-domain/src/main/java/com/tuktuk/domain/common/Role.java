package com.tuktuk.domain.common;

public enum Role {
    PASSENGER,
    DRIVER;

    public String authority() {
        return "ROLE_" + name();
    }
}
