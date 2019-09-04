package com.balita.springexamplecrud.playload.auth;

import lombok.Data;

@Data
public class TokenResponse {
    private String username;
    private String token;
    private Long expiryDuration;
    private String tokenType;

    public TokenResponse() {
    }

    public TokenResponse(String username, String token, Long expiryDuration) {
        this.username = username;
        this.token = token;
        this.expiryDuration = expiryDuration;
        this.tokenType = "Bearer ";
    }
}
