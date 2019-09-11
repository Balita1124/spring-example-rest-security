package com.balita.springexamplecrud.controller;

import com.balita.springexamplecrud.model.Person;
import com.balita.springexamplecrud.playload.ApiResponse;
import com.balita.springexamplecrud.playload.error.ErrorSection;
import com.balita.springexamplecrud.playload.person.PersonRequest;
import com.balita.springexamplecrud.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse> getAllPersons() {
        List<Person> personList = personService.getAll();
        ApiResponse response = new ApiResponse(
                true,
                HttpStatus.OK,
                "Persons List",
                personList
        );
        return new ResponseEntity<ApiResponse>(response, response.getStatus());
    }

    @GetMapping(value = "/persons/{id}", name = "Find a person")
    public ResponseEntity<ApiResponse> getPerson(@PathVariable("id") Long personId) {
        Person person = personService.findPersonById(personId);
        if(person == null){
            ApiResponse response = new ApiResponse(
                    false,
                    HttpStatus.NOT_FOUND,
                    "Person not found",
                    null
            );
        }
        ApiResponse response = new ApiResponse(
                true,
                HttpStatus.OK,
                "Person is in database",
                person
        );
        return new ResponseEntity<ApiResponse>(response, response.getStatus());
    }

    @PostMapping(value = "/persons", name = "Create person")
    public ResponseEntity<ApiResponse> createPerson(@RequestBody @Valid PersonRequest personRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ErrorSection es = new ErrorSection(personRequest, bindingResult.getAllErrors());
            ApiResponse response = new ApiResponse(
                    false,
                    HttpStatus.BAD_REQUEST,
                    "Person not created",
                    es
            );
            return new ResponseEntity<ApiResponse>(response, response.getStatus());
        }
        Person person = personService.create(personRequest);
        ApiResponse response = new ApiResponse(
                true,
                HttpStatus.CREATED,
                "Person Created successfully",
                person
        );
        return new ResponseEntity<ApiResponse>(response, response.getStatus());
    }

    @PutMapping(value = "/persons/{personId}", name = "Update person")
    public ResponseEntity<ApiResponse> updatePerson(@PathVariable(value = "personId") Long personId, @RequestBody @Valid PersonRequest personRequest, BindingResult bindingResult) {

        Person currentPerson = personService.findPersonById(personId);
        if (currentPerson == null) {
            ApiResponse response = new ApiResponse(
                    false,
                    HttpStatus.NOT_FOUND,
                    "Person with id = " + personId + " not found",
                    new ErrorSection(personRequest, null)
            );
            return new ResponseEntity<ApiResponse>(response, response.getStatus());
        }
        if (bindingResult.hasErrors()) {
            ErrorSection es = new ErrorSection(personRequest, bindingResult.getAllErrors());
            ApiResponse response = new ApiResponse(
                    false,
                    HttpStatus.BAD_REQUEST,
                    "Person not updated",
                    es
            );
            return new ResponseEntity<ApiResponse>(response, response.getStatus());
        }
        Person person = personService.update(currentPerson, personRequest);
        ApiResponse response = new ApiResponse(
                true,
                HttpStatus.ACCEPTED,
                "Person Updated successfully",
                person
        );
        return new ResponseEntity<ApiResponse>(response, response.getStatus());
    }

    @DeleteMapping(value = "/persons/{personId}", name = "Delete person")
    public ResponseEntity<ApiResponse> deletePerson(@PathVariable(value = "personId") Long personId) {
        Person currentPerson = personService.findPersonById(personId);
        if (currentPerson == null) {
            ApiResponse response = new ApiResponse(
                    false,
                    HttpStatus.NOT_FOUND,
                    "Person with id = " + personId + " not found",
                    null
            );
            return new ResponseEntity<ApiResponse>(response, response.getStatus());
        }
        personService.delete(currentPerson);
        ApiResponse response = new ApiResponse(
                true,
                HttpStatus.NO_CONTENT,
                "Person Deleted successfully",
                null
        );
        return new ResponseEntity<ApiResponse>(response, response.getStatus());
    }
}
