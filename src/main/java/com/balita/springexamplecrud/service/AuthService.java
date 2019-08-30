package com.balita.springexamplecrud.service;

import com.balita.springexamplecrud.model.User;
import com.balita.springexamplecrud.playload.auth.RegistrationRequest;
import com.balita.springexamplecrud.security.JwtTokenProvider;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.PublicKey;

@Service
public class AuthService {

    private static final Logger logger = Logger.getLogger(AuthService.class);

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthService(UserService userService, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public User registerUser(RegistrationRequest registrationRequest) {
        logger.info("Trying to register new user [" + registrationRequest.getEmail() + "]");
        User user = userService.createUser(registrationRequest);
        return userService.save(user);
    }

    public Boolean emailOrUsernameAlreadyExist(String emailOrUsername) {
        return userService.existsByEmailOrUsername(emailOrUsername, emailOrUsername);
    }

    public Boolean existsByEmail(String email) {
        return userService.existsByEmail(email);
    }

    public Boolean existsByUsername(String username) {
        return userService.existsByUsername(username);
    }


}
