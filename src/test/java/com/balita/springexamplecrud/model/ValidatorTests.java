package com.balita.springexamplecrud.model;

import org.junit.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ValidatorTests {

    private Validator createValidator(){
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        return localValidatorFactoryBean;
    }

    @Test
    public void shouldNotValidateIfLastnameEmpty() throws ParseException {
        LocaleContextHolder.setLocale(Locale.ENGLISH);

        Person person = new Person("", "", new SimpleDateFormat("dd.MM.yyyy").parse("03.02.1990"));

        Validator validator = createValidator();
        Set<ConstraintViolation<Person>> constraintViolations = validator.validate(person);

        assertThat(constraintViolations.size()).isEqualTo(1);

        ConstraintViolation<Person> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("lastname");
        assertThat(violation.getMessage()).isEqualTo("must not be blank");

        assertThat(person.getBirth()).isEqualTo(new SimpleDateFormat("dd.MM.yyyy").parse("03.02.1990"));

    }
}
