package com.balita.springexamplecrud.controller;

import com.balita.springexamplecrud.config.ApplicationTestConfig;
import com.balita.springexamplecrud.exception.ExceptionControllerAdvice;
import com.balita.springexamplecrud.model.Person;
import com.balita.springexamplecrud.playload.person.PersonRequest;
import com.balita.springexamplecrud.service.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationTestConfig.class)
@WebAppConfiguration
public class PersonControllerTests {

    private static final Logger logger = Logger.getLogger(PersonControllerTests.class);

    private MockMvc mockMvc;

    @Autowired
    private PersonController personController;

    @MockBean
    private PersonService personService;

    private List<Person> persons;

    @Before
    public void initPersons() throws ParseException {
        this.mockMvc = MockMvcBuilders.standaloneSetup(personController)
                .setControllerAdvice(new ExceptionControllerAdvice())
                .build();

        this.persons = new ArrayList<>();

        Person person = new Person("Caroline", "Rampy", new SimpleDateFormat("dd.MM.yyyy").parse("03.02.1990"));
        person.setId(3L);
        this.persons.add(person);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetPersonSuccess() throws Exception {
        given(personService.findPersonById(3L)).willReturn(this.persons.get(0));
        logger.info(this.persons.get(0).toString());
        this.mockMvc.perform(get("/api/persons/3").accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(mvcResult -> {
                    logger.info(mvcResult.getResponse().getContentAsString());

                })
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Person is in database"))
                .andExpect(jsonPath("$.data.id").value(3L))
                .andExpect(jsonPath("$.data.firstname").value("Caroline"))
                .andExpect(jsonPath("$.data.lastname").value("Rampy"))
                .andExpect(jsonPath("$.data.birth").value("03.02.1990"))
        ;
    }

    @Test
    public void testGetAllPersons() throws Exception {
        given(this.personService.getAll()).willReturn(this.persons);
        this.mockMvc.perform(get("/api/persons").accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.[0].id").value(3L))
                .andExpect(jsonPath("$.data.[0].firstname").value("Caroline"))
                .andExpect(jsonPath("$.data.[0].lastname").value("Rampy"))
                .andExpect(jsonPath("$.data.[0].birth").value("03.02.1990"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreatePerson() throws Exception {
        PersonRequest personRequest = new PersonRequest(this.persons.get(0));
        logger.info(personRequest.toString());
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(personRequest);
        this.mockMvc.perform(post("/api/persons").content(content).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(mvcResult -> {
                    logger.info(mvcResult.getResponse().getContentAsString());
                })
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreatePerson_Error() throws Exception {
        PersonRequest personRequest = new PersonRequest(this.persons.get(0));
        personRequest.setBirth(null);
        personRequest.setLastname(null);
        logger.info(personRequest.toString());
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(personRequest);
        this.mockMvc.perform(post("/api/persons").content(content).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.data.errors.[0].fieldname").value("lastname"))
                .andExpect(jsonPath("$.data.errors.[0].errorMessage").value("Last name can't blank"))
                .andExpect(jsonPath("$.data.errors.[1].fieldname").value("birth"))
                .andExpect(jsonPath("$.data.errors.[1].errorMessage").value("Date of birth can't null"))
        ;
    }
    @Test
    public void testSupprPersonSuccess() throws Exception {
        given(this.personService.findPersonById(3L)).willReturn(this.persons.get(0));
        this.mockMvc.perform(delete("/api/persons/3").accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());
    }
}
