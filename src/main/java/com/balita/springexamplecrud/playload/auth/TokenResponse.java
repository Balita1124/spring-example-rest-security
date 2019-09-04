package com.balita.springexamplecrud.playload.auth;

import lombok.Data;

@Data
public class TokenResponse {
    private String usernameOrEmail;
    private String token;
    private String refreshToken;
    private Long expiryDuration;
    private String tokenType;

    public TokenResponse() {
    }

    public TokenResponse(String usernameOrEmail, String token, String refreshToken, Long expiryDuration) {
        this.usernameOrEmail = usernameOrEmail;
        this.token = token;
        this.refreshToken = refreshToken;
        this.expiryDuration = expiryDuration;
        this.tokenType = "Bearer ";
    }
}
