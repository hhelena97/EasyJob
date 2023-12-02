package de.hbrs.easyjob;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Hauptklasse der Anwendung
 * @see <a href="https://vaadin.com/docs/v23/advanced/modifying-the-bootstrap-page#application-shell">Vaadin Application Shell</a>
 */

@SpringBootApplication
@PWA(
		name = "EasyJob - Easy gesucht, easy gefunden",
		shortName = "EasyJob"
)
public class EasyjobApplication extends SpringBootServletInitializer implements AppShellConfigurator {

	public static void main(String[] args) {
		SpringApplication.run(EasyjobApplication.class, args);
	}

}
