package com.balita.springexamplecrud.controller;

import com.balita.springexamplecrud.config.ApplicationTestConfig;
import com.balita.springexamplecrud.controller.AuthController;
import com.balita.springexamplecrud.exception.ExceptionControllerAdvice;
import com.balita.springexamplecrud.model.DeviceType;
import com.balita.springexamplecrud.model.Role;
import com.balita.springexamplecrud.model.RoleName;
import com.balita.springexamplecrud.model.User;
import com.balita.springexamplecrud.playload.DeviceInfo;
import com.balita.springexamplecrud.playload.auth.LoginRequest;
import com.balita.springexamplecrud.service.AuthService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = ApplicationTestConfig.class)
@WebAppConfiguration
public class AuthControllerTests {
    private MockMvc mockMvc;

    @Autowired
    private AuthController authController;

    @MockBean
    private AuthService authService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    User gUser;

    DeviceInfo deviceInfo;

    @Before
    public void initAuth() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(new ExceptionControllerAdvice())
                .build();
        Role roleAdmin = new Role(RoleName.ROLE_ADMIN);
        gUser = new User();
        gUser.setId(1L);
        gUser.setUsername("balita");
        gUser.setEmail("ricomroatfauchard@gmail.com");
        gUser.setPassword(passwordEncoder.encode("0123456789"));
        gUser.setActive(true);
        gUser.setIsEmailVerified(true);
        gUser.addRole(roleAdmin);

        deviceInfo.setDeviceId("0000");
        deviceInfo.setDeviceType(DeviceType.DEVICE_TYPE_BROWSER);
        deviceInfo.setNotificationToken(null);
    }


}
