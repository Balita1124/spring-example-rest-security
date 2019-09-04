package com.balita.springexamplecrud.validation.validator;

import com.balita.springexamplecrud.service.UserService;
import com.balita.springexamplecrud.validation.annotation.UniqUsername;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqUsernameValidator implements ConstraintValidator<UniqUsername, String> {

    private UserService userService;

    @Autowired
    public UniqUsernameValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void initialize(UniqUsername constraintAnnotation) {

    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        return !(userService.existsByUsername(username));
    }
}
