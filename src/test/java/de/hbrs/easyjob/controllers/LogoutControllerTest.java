package de.hbrs.easyjob.controllers;

import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.VaadinServletResponse;
import com.vaadin.flow.server.VaadinSession;
import de.hbrs.easyjob.security.CustomSecurityContextRepository;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class LogoutControllerTest {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomSecurityContextRepository customSecurityContextRepository;

    // Controllers
    @Autowired
    private LogoutController logoutController;

    @BeforeAll
    static void setUp() {

    }

    @BeforeEach
    void setUpEach() {
    }

    @AfterAll
    static void tearDown() {
    }

    @Test
    @DisplayName("Testet den Logout mit angemeldeter/authentifizierter Person")
    void logoutTest() {
        // ************** Arrange **************
        // Einloggen mit Testdaten (siehe LoginView.java)
        String username = "julia.weber@uni-hamburg.de";
        String password = "Studium2023!";

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(authentication);

        VaadinServletRequest mockRequest = Mockito.mock(VaadinServletRequest.class);
        VaadinServletResponse mockResponse = Mockito.mock(VaadinServletResponse.class);
        VaadinSession mockSession = Mockito.mock(VaadinSession.class);

        Mockito.mockStatic(VaadinSession.class).when(VaadinSession::getCurrent).thenReturn(mockSession);

        SecurityContext[] securityContext = {sc};

        Mockito.when(mockSession.getAttribute(SecurityContext.class)).thenReturn(securityContext[0]);
        
        Mockito.doAnswer(invocation -> {
            securityContext[0] = invocation.getArgument(1);
            return null;
        }).when(mockSession).setAttribute(Mockito.eq(SecurityContext.class), Mockito.any(SecurityContext.class));

        customSecurityContextRepository.saveContext(sc, mockRequest, mockResponse);

        assertNotNull(customSecurityContextRepository.loadContext(mockRequest).get().getAuthentication());

        // **************** Act ****************
        // Ausloggen
        logoutController.logout(mockRequest, mockResponse);

        // ************** Assert ***************
        // Überprüfen, ob die Person ausgeloggt ist
        assertNull(customSecurityContextRepository.loadContext(mockRequest).get().getAuthentication());
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