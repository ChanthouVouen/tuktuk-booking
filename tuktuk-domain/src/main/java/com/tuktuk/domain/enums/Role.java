package com.tuktuk.domain.enums;

public enum Role {
    PASSENGER,
    DRIVER;

    public String authority() {
        return "ROLE_" + name();
    }
}
