package de.hbrs.easyjob.security;


import com.vaadin.flow.server.VaadinSession;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.core.context.SecurityContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class CustomSecurityContextRepository extends HttpSessionSecurityContextRepository {
    private static final Logger logger = LoggerFactory.getLogger(CustomSecurityContextRepository.class);

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        logger.debug("Speichere SecurityContext für die Anfrage");
        super.saveContext(context, request, response);

        VaadinSession.getCurrent().setAttribute(SecurityContext.class, context);
    }

    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        logger.debug("Lade SecurityContext für die Anfrage");
        SecurityContext context = super.loadContext(requestResponseHolder);


        SecurityContext vaadinContext = VaadinSession.getCurrent().getAttribute(SecurityContext.class);
        return vaadinContext != null ? vaadinContext : context;
    }
}
