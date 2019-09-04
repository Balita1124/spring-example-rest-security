package com.balita.springexamplecrud.service;

import com.balita.springexamplecrud.model.CustomUserDetails;
import com.balita.springexamplecrud.model.RefreshToken;
import com.balita.springexamplecrud.model.User;
import com.balita.springexamplecrud.model.UserDevice;
import com.balita.springexamplecrud.playload.auth.LoginRequest;
import com.balita.springexamplecrud.playload.auth.RegistrationRequest;
import com.balita.springexamplecrud.security.JwtTokenProvider;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
import java.util.Optional;

@Service
public class AuthService {

    private static final Logger logger = Logger.getLogger(AuthService.class);

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDeviceService userDeviceService;
    private final RefreshTokenService refreshTokenService;

    @Autowired
    public AuthService(UserService userService, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, UserDeviceService userDeviceService, RefreshTokenService refreshTokenService) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userDeviceService = userDeviceService;
        this.refreshTokenService = refreshTokenService;
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


    public Authentication authenticateUser(LoginRequest loginRequest){
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword()));
    }

    public RefreshToken createAndPersistRefreshTokenForDevice(Authentication authentication, LoginRequest loginRequest){
        User currentUser = (User) authentication.getPrincipal();
        userDeviceService.findByUserId(currentUser.getId())
                .map(UserDevice::getRefreshToken)
                .map(RefreshToken::getId)
                .ifPresent(refreshTokenService::deleteById);

        UserDevice userDevice = userDeviceService.createUserDevice(loginRequest.getDeviceInfo());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken();
        userDevice.setUser(currentUser);
        userDevice.setRefreshToken(refreshToken);
        refreshToken.setUserDevice(userDevice);
        refreshToken = refreshTokenService.save(refreshToken);
        return refreshToken;

    }


    public String generateToken(CustomUserDetails customUserDetails) {
        return jwtTokenProvider.generateToken(customUserDetails);
    }
}
