package com.balita.springexamplecrud.playload.auth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "Login Request", description = "The login request playload")
public class LoginRequest {

    @NotNull
    @ApiModelProperty(value = "User registered username or email", required = true, allowableValues = "NonEmpty String")
    private String usernameOrEmail;

    @NotNull
    @ApiModelProperty(value = "Valid user password", required = true, allowableValues = "NonEmpty String")
    private String password;

    public LoginRequest() {
    }

    public LoginRequest(@NotNull String usernameOrEmail, @NotNull String password) {
        this.usernameOrEmail = usernameOrEmail;
        this.password = password;
    }
}
