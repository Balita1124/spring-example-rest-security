package com.balita.springexamplecrud.playload.auth;

import com.balita.springexamplecrud.playload.DeviceInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
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

    @Valid
    @NotNull(message = "Device info cannot be null")
    @ApiModelProperty(value = "Device info", required = true, dataType = "object", allowableValues = "A valid " +
            "deviceInfo object")
    private DeviceInfo deviceInfo;

    public LoginRequest() {
    }

    public LoginRequest(@NotNull String usernameOrEmail, @NotNull String password, DeviceInfo deviceInfo) {
        this.usernameOrEmail = usernameOrEmail;
        this.password = password;
        this.deviceInfo = deviceInfo;
    }
}
