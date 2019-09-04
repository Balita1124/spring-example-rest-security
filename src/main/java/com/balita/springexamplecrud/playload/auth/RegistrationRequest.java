package com.balita.springexamplecrud.playload.auth;

import com.balita.springexamplecrud.validation.annotation.UniqMail;
import com.balita.springexamplecrud.validation.annotation.UniqUsername;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "Registration Request", description = "The registration request payload")
public class RegistrationRequest {

    @NotBlank(message = "Registration username can not blank")
    @UniqUsername(message = "Username must be uniq")
    @ApiModelProperty(value = "A valid username", allowableValues = "NonEmpty String")
    private String username;

    @NotBlank(message = "Registration email can not blank")
    @UniqMail(message = "Email must be uniq")
    @ApiModelProperty(value = "A valid email", required = true, allowableValues = "NonEmpty String")
    private String email;

    @NotNull(message = "Registration password cannot be null")
    @ApiModelProperty(value = "A valid password string", required = true, allowableValues = "NonEmpty String")
    private String password;

    @NotNull(message = "Specify whether the user has to be registered as an admin or not")
    @ApiModelProperty(value = "Flag denoting whether the user is an admin or not", required = true,
            dataType = "boolean", allowableValues = "true, false")
    private Boolean registerAsAdmin;
}
