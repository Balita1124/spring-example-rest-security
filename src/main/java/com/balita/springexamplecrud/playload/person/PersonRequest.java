package com.balita.springexamplecrud.playload.person;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class PersonRequest {

    private String firstname;

    @NotBlank(message = "Last name can't blank")
    private String lastname;

    @JsonFormat(pattern="dd-MM-yyyy")
    @NotNull(message = "Date of birth")
    private Date birth;
}
