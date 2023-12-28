package de.hbrs.easyjob.controllers;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.SecurityConfig;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SecurityConfig.class)
@WebAppConfiguration
class LogoutControllerTest {
    // Mockito
    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    // Controllers
    private LogoutController logout;

    @BeforeAll
    void setUp() {

    }

    @BeforeEach
    void setUpEach() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        logout  = new LogoutController();
    }

    @AfterEach
    void tearDownEach() {
        logout = null;
    }

    @AfterAll
    static void tearDown() {
    }

    @Test
    @DisplayName("Testet den Logout mit angemeldeter/authentifizierter Person")
    void logoutTest() {
        // ************** Arrange **************

        // **************** Act ****************

        // ************** Assert ***************
    }

    @Test
    @DisplayName("Testet den Logout (wieso auch immer) mit nicht angemeldeter Person")
    void strangeLogoutTest() {
        // ************** Arrange **************

        // **************** Act ****************

        // ************** Assert ***************
    }

    @Test
    @DisplayName("Testet den Logout mit deaktiviertem Profil")
    void deactivateLogoutTest() {
        // ************** Arrange **************

        // **************** Act ****************

        // ************** Assert ***************
    }
}