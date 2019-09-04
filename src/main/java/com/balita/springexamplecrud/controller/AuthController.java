package com.balita.springexamplecrud.controller;


import com.balita.springexamplecrud.model.CustomUserDetails;
import com.balita.springexamplecrud.model.User;
import com.balita.springexamplecrud.playload.ApiResponse;
import com.balita.springexamplecrud.playload.auth.LoginRequest;
import com.balita.springexamplecrud.playload.auth.RegistrationRequest;
import com.balita.springexamplecrud.playload.error.ErrorSection;
import com.balita.springexamplecrud.security.JwtTokenProvider;
import com.balita.springexamplecrud.service.AuthService;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    public static final Logger logger = Logger.getLogger(AuthController.class);

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ApplicationEventPublisher applicationEventPublisher;

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
        }
        User user = authService.registerUser(registrationRequest);
        return new ApiResponse(
                true,
                HttpStatus.OK,
                "User registered",
                user
        );
    }
    @PostMapping("/login")
    public ApiResponse authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            ErrorSection es = new ErrorSection(loginRequest, bindingResult.getAllErrors());
            return new ApiResponse(
                    false,
                    HttpStatus.OK,
                    "User not logged",
                    es

            );
        }

        Authentication authentication = authService.authenticateUser(loginRequest);
        System.out.println(authentication.toString());
        if(!authentication.isAuthenticated()){
            ErrorSection es = new ErrorSection(loginRequest, bindingResult.getAllErrors());
            return new ApiResponse(
                    false,
                    HttpStatus.OK,
                    "Username or Email and Password is incorrect",
                    null
            );
        }
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        logger.info("Logged in User returned [API]: " + customUserDetails.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        RefreshTo
    }
}
