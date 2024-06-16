package com.swp391.jewelrysalesystem.models;

import lombok.Getter;

@Getter
public class AuthenticationResponse {
    private final String jwt;
    private final int status;

    public AuthenticationResponse(String jwt, int status) {
        this.jwt = jwt;
        this.status = status;
    }
}
