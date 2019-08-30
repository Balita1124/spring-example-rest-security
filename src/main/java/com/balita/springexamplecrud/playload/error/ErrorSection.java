package com.balita.springexamplecrud.playload.error;

import com.balita.springexamplecrud.playload.error.CustomFieldError;
import lombok.Data;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

@Data
public class ErrorSection {
    Object request;
    List<?> errors;

    public ErrorSection(Object request, List<ObjectError> errors) {
        List<CustomFieldError> listErrors = new ArrayList<>();
        if (errors != null)
            errors.forEach(objectError -> listErrors.add(new CustomFieldError(((FieldError) objectError).getField(), objectError.getDefaultMessage())));
        this.request = request;
        this.errors = listErrors;
    }
}
