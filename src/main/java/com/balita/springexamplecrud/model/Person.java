package com.balita.springexamplecrud.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "PERSONS")
@Data
public class Person extends DateAudit{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstname;

    @NotBlank
    private String lastname;

    @JsonFormat(pattern = "dd-MM-yyyy")
    @NotNull
    private Date birth;

    public Person() {
    }

    public Person(String firstname, String lastname, Date birth) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.birth = birth;
    }
}
