package com.balita.springexamplecrud.playload.auth;

import com.balita.springexamplecrud.model.Role;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

public class UserResponse {
    private Long id;
    private String email;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private Boolean active;
    private Set<Role> roles = new HashSet<>();
    private Boolean isEmailVerified;
}
