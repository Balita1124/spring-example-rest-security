package com.balita.springexamplecrud;

import com.balita.springexamplecrud.model.DeviceType;
import com.balita.springexamplecrud.playload.DeviceInfo;
import com.balita.springexamplecrud.playload.auth.LoginRequest;
import com.balita.springexamplecrud.playload.person.PersonRequest;
import com.balita.springexamplecrud.service.AuthService;
import com.balita.springexamplecrud.service.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.hamcrest.Matchers.is;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PersonControllerTest {

    private static String BASE_URL = "api";

    private static final Logger LOG = Logger.getLogger(PersonControllerTest.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    private MockMvc mvc;

    @MockBean
    AuthService authService;

    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void initTests() {
        // Always start from known state, in this case 1 row in hero table.
        jdbcTemplate.execute("delete from persons;");
    }

    @Test
    public void contextLoads() {
        assertThat(jdbcTemplate).isNotNull();
        assertThat(mvc).isNotNull();
    }

    @Test
//    @WithMockUser(username = "balita")
    public void shouldCreatePerson() throws Exception {
        PersonRequest personRequest = new PersonRequest("Rico", "Fauchard", new Date(1992, 9, 15));
        byte[] personJson = toJson(personRequest);
        MvcResult result = invokeCreatePerson(personJson, "")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(false)))
                .andReturn();
    }
//    @Test
//    public void shouldLogged() throws Exception {
//        LoginRequest loginRequest = new LoginRequest("balita", "123456789", new DeviceInfo("123456", DeviceType.DEVICE_TYPE_BROWSER));
//        byte[] loginJson = toJson(loginRequest);
//        MvcResult resultLogin = invokeLogin(loginJson)
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success", is(true)))
//                .andReturn();
//    }

    private ResultActions invokeCreatePerson(byte[] personJson, String token) throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(token);
        return mvc.perform(post(BASE_URL + "/persons").content(personJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
    }

    private ResultActions invokeLogin(byte[] loginJson) throws Exception {
        return mvc.perform(post(BASE_URL + "/auth/login").content(loginJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
    }

    <T> T fromJsonResult(MvcResult result,
                         Class<T> tClass) throws Exception {
        return this.mapper.readValue(
                result.getResponse().getContentAsString(),
                tClass);
    }

    private byte[] toJson(Object object) throws Exception {
        return this.mapper
                .writeValueAsString(object).getBytes();
    }

}
