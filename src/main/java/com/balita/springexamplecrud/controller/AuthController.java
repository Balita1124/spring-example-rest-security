package com.balita.springexamplecrud.controller;


import com.balita.springexamplecrud.model.User;
import com.balita.springexamplecrud.playload.ApiResponse;
import com.balita.springexamplecrud.playload.auth.RegistrationRequest;
import com.balita.springexamplecrud.playload.error.ErrorSection;
import com.balita.springexamplecrud.security.JwtTokenProvider;
import com.balita.springexamplecrud.service.AuthService;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    public static final Logger logger = Logger.getLogger(AuthController.class);

    public final AuthService authService;
    public final JwtTokenProvider jwtTokenProvider;
    public final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public AuthController(AuthService authService, JwtTokenProvider jwtTokenProvider, ApplicationEventPublisher applicationEventPublisher) {
        this.authService = authService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.applicationEventPublisher = applicationEventPublisher;
    }


    @PostMapping("/register")
    @ApiOperation(value = "Registers the user and publishes an event to generate the email verification")
    public ApiResponse registerUser(@Valid @RequestBody RegistrationRequest registrationRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ErrorSection es = new ErrorSection(registrationRequest, bindingResult.getAllErrors());
            return new ApiResponse(
                    false,
                    HttpStatus.OK,
                    "User not registered",
                    es

            );
        } else {
            if (authService.existsByEmail(registrationRequest.getEmail()) || authService.existsByUsername(registrationRequest.getUsername())) {
                bindingResult.addError(new ObjectError("User", "Email or Username must be unique"));
                ErrorSection es = new ErrorSection(registrationRequest, bindingResult.getAllErrors());
                return new ApiResponse(
                        false,
                        HttpStatus.OK,
                        "User not registered",
                        es

                );
            }
        }
        User user = authService.registerUser(registrationRequest);
        return new ApiResponse(
                true,
                HttpStatus.OK,
                "User registered",
                user
        );
    }
}
