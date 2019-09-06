package com.balita.springexamplecrud.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.mockito.MockitoAnnotations;
@TestConfiguration
public class ApplicationTestConfig {
    public ApplicationTestConfig(){
        MockitoAnnotations.initMocks(this);
    }
}
