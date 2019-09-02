package com.balita.springexamplecrud.validation.validator;


import com.balita.springexamplecrud.playload.auth.RegistrationRequest;
import com.balita.springexamplecrud.service.AuthService;
import com.balita.springexamplecrud.validation.annotation.UserMustBeUnique;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserMustBeUniqueValidator implements ConstraintValidator<UserMustBeUnique, Object> {

    private AuthService authService;
    private String field;

    @Autowired
    public UserMustBeUniqueValidator(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void initialize(UserMustBeUnique constraintAnnotation) {
        this.field = constraintAnnotation.field();
    }

    @Override
    public boolean isValid(RegistrationRequest registrationRequest, ConstraintValidatorContext constraintValidatorContext) {
        return !(authService.existsByEmail(registrationRequest.getEmail()) || authService.existsByUsername(registrationRequest.getUsername()));
    }
}
