package com.balita.springexamplecrud.controller;


import com.balita.springexamplecrud.model.CustomUserDetails;
import com.balita.springexamplecrud.model.RefreshToken;
import com.balita.springexamplecrud.model.User;
import com.balita.springexamplecrud.playload.ApiResponse;
import com.balita.springexamplecrud.playload.auth.LoginRequest;
import com.balita.springexamplecrud.playload.auth.RegistrationRequest;
import com.balita.springexamplecrud.playload.auth.TokenResponse;
import com.balita.springexamplecrud.playload.error.ErrorSection;
import com.balita.springexamplecrud.security.JwtTokenProvider;
import com.balita.springexamplecrud.service.AuthService;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody RegistrationRequest registrationRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ErrorSection es = new ErrorSection(registrationRequest, bindingResult.getAllErrors());
            ApiResponse response = new ApiResponse(
                    false,
                    HttpStatus.BAD_REQUEST,
                    "User not registered",
                    es
            );
            return new ResponseEntity<ApiResponse>(response, response.getStatus());
        }
        User user = authService.registerUser(registrationRequest);
        ApiResponse response = new ApiResponse(
                true,
                HttpStatus.CREATED,
                "User registered",
                user
        );
        return new ResponseEntity<ApiResponse>(response, response.getStatus());
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ErrorSection es = new ErrorSection(loginRequest, bindingResult.getAllErrors());
            ApiResponse response = new ApiResponse(
                    false,
                    HttpStatus.NOT_ACCEPTABLE,
                    "User not logged",
                    es
            );
            return new ResponseEntity<ApiResponse>(response, response.getStatus());
        }

        Authentication authentication = authService.authenticateUser(loginRequest);
        if (!authentication.isAuthenticated()) {
            ApiResponse response = new ApiResponse(
                    false,
                    HttpStatus.BAD_REQUEST,
                    "Username or Email and Password is incorrect",
                    null
            );
            return new ResponseEntity<ApiResponse>(response, response.getStatus());
        }
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        logger.info("Logged in User returned [API]: " + customUserDetails.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        RefreshToken refreshTokenObject = authService.createAndPersistRefreshTokenForDevice(authentication, loginRequest);
        String refreshToken = refreshTokenObject.getToken();
        String jwtToken = authService.generateToken(customUserDetails);
        TokenResponse tokenResponse = new TokenResponse(customUserDetails.getUsername(), jwtToken, refreshToken, jwtTokenProvider.getExpiryDuration());
        ApiResponse response = new ApiResponse(
                true,
                HttpStatus.ACCEPTED,
                "User Logged",
                tokenResponse

        );
        return new ResponseEntity<ApiResponse>(response, response.getStatus());
    }
}
