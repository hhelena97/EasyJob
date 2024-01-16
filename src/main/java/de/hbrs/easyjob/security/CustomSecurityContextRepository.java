package de.hbrs.easyjob.security;


import com.vaadin.flow.server.VaadinSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.function.Supplier;

@Repository
public class CustomSecurityContextRepository extends HttpSessionSecurityContextRepository {
    private static final Logger logger = LoggerFactory.getLogger(CustomSecurityContextRepository.class);

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        logger.debug("Speichere SecurityContext für die Anfrage");
        super.saveContext(context, request, response);

        VaadinSession.getCurrent().setAttribute(SecurityContext.class, context);
    }

    @Override
    public Supplier<SecurityContext> loadContext(HttpServletRequest requestResponseHolder) {
        logger.debug("Lade SecurityContext für die Anfrage");
        return super.loadContext(requestResponseHolder);
    }

    public void clearContext() {
        VaadinSession session = VaadinSession.getCurrent();
        if (session != null) {
            session.setAttribute(SecurityContext.class, null);
        }
    }
}
