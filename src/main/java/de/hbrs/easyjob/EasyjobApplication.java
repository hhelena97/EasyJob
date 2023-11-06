package de.hbrs.easyjob;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@PWA(
		name = "EasyJob - Easy gesucht, easy gefunden",
		shortName = "EasyJob"
)
public class EasyjobApplication implements AppShellConfigurator {

	public static void main(String[] args) {
		SpringApplication.run(EasyjobApplication.class, args);
	}

}
