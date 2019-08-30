package com.balita.springexamplecrud.controller;

import com.balita.springexamplecrud.model.Person;
import com.balita.springexamplecrud.playload.ApiResponse;
import com.balita.springexamplecrud.playload.error.ErrorSection;
import com.balita.springexamplecrud.playload.person.PersonRequest;
import com.balita.springexamplecrud.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PersonController {

    private PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping(value = "/persons", name = "Find All person")
    public ApiResponse getAllPersons() {
        List<Person> personList = personService.getAll();
        return new ApiResponse(
                true,
                HttpStatus.OK,
                "Persons List",
                personList
        );
    }

    @PostMapping(value = "/persons", name = "Create person")
    public ApiResponse createPerson(@RequestBody @Valid PersonRequest personRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ErrorSection es = new ErrorSection(personRequest, bindingResult.getAllErrors());
            return new ApiResponse(
                    false,
                    HttpStatus.OK,
                    "Person not created",
                    es
            );
        }
        Person person = personService.create(personRequest);
        return new ApiResponse(
                true,
                HttpStatus.OK,
                "Person Created successfully",
                person
        );
    }

    @PutMapping(value = "/persons/{personId}", name = "Update person")
    public ApiResponse updatePerson(@PathVariable(value = "personId") Long personId, @RequestBody @Valid PersonRequest personRequest, BindingResult bindingResult) {

        Person currentPerson = personService.findPersonById(personId);
        if (currentPerson == null) {
            return new ApiResponse(
                    false,
                    HttpStatus.OK,
                    "Person with id = " + personId + " not found",
                    new ErrorSection(personRequest, null)
            );
        }
        if (bindingResult.hasErrors()) {
            ErrorSection es = new ErrorSection(personRequest, bindingResult.getAllErrors());
            return new ApiResponse(
                    false,
                    HttpStatus.OK,
                    "Person not updated",
                    es
            );
        }
        Person person = personService.update(currentPerson, personRequest);
        return new ApiResponse(
                true,
                HttpStatus.OK,
                "Person Updated successfully",
                person
        );
    }

    @DeleteMapping(value = "/persons/{personId}", name = "Delete person")
    public ApiResponse deletePerson(@PathVariable(value = "personId") Long personId) {
        Person currentPerson = personService.findPersonById(personId);
        if (currentPerson == null) {
            return new ApiResponse(
                    false,
                    HttpStatus.OK,
                    "Person with id = " + personId + " not found",
                    null
            );
        }
        personService.delete(currentPerson);
        return new ApiResponse(
                true,
                HttpStatus.OK,
                "Person Deleted successfully",
                null
        );
    }
}
