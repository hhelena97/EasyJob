package de.hbrs.easyjob;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.VaadinServiceInitListener;
import de.hbrs.easyjob.security.CustomVaadinServiceInitListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import com.vaadin.collaborationengine.CollaborationEngineConfiguration;
import com.vaadin.collaborationengine.CollaborationEngineConfiguration;
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
		SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
		SpringApplication.run(EasyjobApplication.class, args);
	}

	@Bean
	public VaadinServiceInitListener vaadinServiceInitListener() {
		return new CustomVaadinServiceInitListener();
	}

	@Bean
	public CollaborationEngineConfiguration ceConfigBean() {
		CollaborationEngineConfiguration configuration = new CollaborationEngineConfiguration(
				licenseEvent -> {
					// See <<ce.production.license-events>>
				});
		configuration.setDataDir("/src/main/java/de/hbrs/easyjob/collaborationEngine/");
		return configuration;
	}

}
