package de.hbrs.easyjob.security;

import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.SessionInitListener;
import org.springframework.security.core.context.SecurityContext;

public class CustomVaadinServiceInitListener implements VaadinServiceInitListener {

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addSessionInitListener((SessionInitListener) event1 -> VaadinSession.getCurrent().setAttribute(SecurityContext.class, null));
    }
}
