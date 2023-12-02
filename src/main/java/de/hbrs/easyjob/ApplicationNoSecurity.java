package de.hbrs.easyjob;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;

/**
 * Konfiguration für die Anwendung ohne Sicherheit
 * Bitte ausschließlich für Entwicklungszwecke verwenden!
 */
@Configuration
@Profile("insecure")
public class ApplicationNoSecurity {

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .antMatchers("/**");
    }
}