package com.balita.springexamplecrud.playload.person;

import com.balita.springexamplecrud.model.Person;
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

    @JsonFormat(pattern="dd.MM.yyyy")
    @NotNull(message = "Date of birth can't null")
    private Date birth;

    public PersonRequest() {
    }

    public PersonRequest(String firstname, @NotBlank(message = "Last name can't blank") String lastname, @NotNull(message = "Date of birth") Date birth) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.birth = birth;
    }

    public PersonRequest(Person person) {
        this.firstname = person.getFirstname();
        this.lastname = person.getLastname();
        this.birth = person.getBirth();
    }
}
