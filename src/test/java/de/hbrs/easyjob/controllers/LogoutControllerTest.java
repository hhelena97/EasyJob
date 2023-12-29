package de.hbrs.easyjob.controllers;

import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.VaadinServletResponse;
import com.vaadin.flow.server.VaadinSession;
import de.hbrs.easyjob.security.CustomSecurityContextRepository;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@SuppressWarnings("unchecked")
class LogoutControllerTest {
    // Manager und Repository aus der LoginView, um einen Login zu simulieren
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CustomSecurityContextRepository customSecurityContextRepository;

    // Controller
    @Autowired
    private LogoutController logoutController;

    // Mockito
    private MockedStatic mockedStatic;
    private VaadinServletResponse mockResponse;
    private VaadinServletRequest mockRequest;
    private VaadinSession mockSession;

    @BeforeEach
    void setUpEach() {
        // VaadinSession mocken
        mockRequest = Mockito.mock(VaadinServletRequest.class);
        mockResponse = Mockito.mock(VaadinServletResponse.class);
        mockSession = Mockito.mock(VaadinSession.class);

        // Hier wird festgelegt, dass, sobald man eine VaadinSession abfragt, die gemockte VaadinSession ausgegeben wird
        mockedStatic = Mockito.mockStatic(VaadinSession.class);
        mockedStatic.when(VaadinSession::getCurrent).thenReturn(mockSession);
    }

    @AfterEach
    void tearDownEach() {
        mockedStatic.close();
    }

    @Test
    @DisplayName("Testet den Logout mit angemeldeter/authentifizierter Person")
    void logoutTest() {
        // ************** Arrange **************

        /*
         * Hier wird als 1. Schritt der Login wie in der LoginView simuliert, damit man eine authentifizierte Person
         * hat, mit der der Logout getestet wird.
         */

        // Testdaten von einer Person, die in der Datenbank existiert
        String username_echte_person = "nina.becker@uni-bonn.de";
        String password_echte_person = "NinaMINT2023";

        // Authentifizierungsschritt aus der LoginView
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username_echte_person, password_echte_person)
        );

        // SecurityContext definieren und authentication setzen
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        SecurityContext[] securityContextField = {securityContext};

        // Hier soll der zuvor definierte SecurityContext der gemockten Session zugeordnet werden
        Mockito.when(mockSession.getAttribute(SecurityContext.class)).thenReturn(securityContextField[0]);

        Mockito.doAnswer(invocation -> {
            securityContextField[0] = invocation.getArgument(1);
            return null;
        }).when(mockSession).setAttribute(Mockito.eq(SecurityContext.class), Mockito.any(SecurityContext.class));

        // Speicherung des gemockten SecurityContexts, Request und Response in der costumSecurityContextRepository
        customSecurityContextRepository.saveContext(securityContext, mockRequest, mockResponse);

        // Überprüfung, ob das Mocken des Login geklappt hat
        assertNotNull(customSecurityContextRepository.loadContext(mockRequest).get().getAuthentication());

        // **************** Act ****************
        logoutController.logout(mockRequest, mockResponse);

        // ************** Assert ***************
        assertNull(customSecurityContextRepository.loadContext(mockRequest).get().getAuthentication());
    }

    @Test
    @DisplayName("Testet den Logout (wieso auch immer) mit nicht angemeldeter Person")
    void strangeLogoutTest() {
        // ************** Arrange **************

        /*
         * Hier wird kein Login simuliert. Eigentlich sollte dieser Fall nicht eintreffen, aber falls doch, sollte der
         * abgefangen werden.
         *
         * Im Moment kann man sich normal ausloggen und es funktioniert ohne Fehlermeldung.
         */

        // SecurityContext definieren und authentication setzen
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(null);

        SecurityContext[] securityContextField = {securityContext};

        // VaadinSession mocken
        VaadinServletRequest mockRequest = Mockito.mock(VaadinServletRequest.class);
        VaadinServletResponse mockResponse = Mockito.mock(VaadinServletResponse.class);
        VaadinSession mockSession = Mockito.mock(VaadinSession.class);

        // Hier soll der zuvor definierte SecurityContext der gemockten Session zugeordnet werden
        Mockito.when(mockSession.getAttribute(SecurityContext.class)).thenReturn(securityContextField[0]);

        Mockito.doAnswer(invocation -> {
            securityContextField[0] = invocation.getArgument(1);
            return null;
        }).when(mockSession).setAttribute(Mockito.eq(SecurityContext.class), Mockito.any(SecurityContext.class));

        // Speicherung des gemockten SecurityContexts, Request und Response in der costumSecurityContextRepository
        customSecurityContextRepository.saveContext(securityContext, mockRequest, mockResponse);

        // **************** Act ****************
        logoutController.logout(mockRequest, mockResponse);

        // ************** Assert ***************
        assertNull(customSecurityContextRepository.loadContext(mockRequest).get().getAuthentication());
    }

    @Test
    @DisplayName("Testet den Logout mit deaktiviertem Profil")
    void deactivateLogoutTest() {
        // ************** Arrange **************

        /*
         * Nun wird sich mit einem deaktivierten Profil eingeloggt, hier muss also auch erst einmal wieder der Login
         * simuliert werden
         */

        // Testdaten von einer deaktivierten Person, die in der Datenbank existiert
        String username_echte_person = "helena-heyen@email.de";
        String password_echte_person = "Test123!";

        // Authentifizierungsschritt aus der LoginView
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username_echte_person, password_echte_person)
        );

        // SecurityContext definieren und authentication setzen
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        SecurityContext[] securityContextField = {securityContext};

        // Hier soll der zuvor definierte SecurityContext der gemockten Session zugeordnet werden
        Mockito.when(mockSession.getAttribute(SecurityContext.class)).thenReturn(securityContextField[0]);

        Mockito.doAnswer(invocation -> {
            securityContextField[0] = invocation.getArgument(1);
            return null;
        }).when(mockSession).setAttribute(Mockito.eq(SecurityContext.class), Mockito.any(SecurityContext.class));

        // Speicherung des gemockten SecurityContexts, Request und Response in der costumSecurityContextRepository
        customSecurityContextRepository.saveContext(securityContext, mockRequest, mockResponse);

        // Überprüfung, ob das Mocken des Login geklappt hat
        assertNotNull(customSecurityContextRepository.loadContext(mockRequest).get().getAuthentication());

        // **************** Act ****************
        logoutController.logout(mockRequest, mockResponse);

        // ************** Assert ***************
        assertNull(customSecurityContextRepository.loadContext(mockRequest).get().getAuthentication());
    }
}