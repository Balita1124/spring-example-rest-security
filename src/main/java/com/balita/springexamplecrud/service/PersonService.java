package com.balita.springexamplecrud.service;

import com.balita.springexamplecrud.model.Person;
import com.balita.springexamplecrud.playload.person.PersonRequest;
import com.balita.springexamplecrud.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonService {

    private PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> getAll() {
        List<Person> personList = new ArrayList<>();
        personRepository.findAll().forEach(personList::add);
        return personList;
    }

    public Person create(PersonRequest personRequest) {
        Person person = new Person(personRequest.getFirstname(), personRequest.getLastname(), personRequest.getBirth());
        return personRepository.save(person);

    }

    public Person update(Person currentPerson, PersonRequest personRequest) {
        currentPerson.setFirstname(personRequest.getFirstname());
        currentPerson.setLastname(personRequest.getLastname());
        currentPerson.setBirth(personRequest.getBirth());
        return personRepository.save(currentPerson);

    }

    public Person findPersonById(Long personId) {
        return personRepository.findById(personId).orElse(null);
    }

    public void delete(Person person) {
        personRepository.delete(person);
    }
}
