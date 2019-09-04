package com.balita.springexamplecrud.validation.validator;

import com.balita.springexamplecrud.service.UserService;
import com.balita.springexamplecrud.validation.annotation.UniqMail;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqMailValidator implements ConstraintValidator<UniqMail, String> {


    private UserService userService;

    @Autowired
    public UniqMailValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void initialize(UniqMail constraintAnnotation) {

    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return !(userService.existsByEmail(email));
    }
}
