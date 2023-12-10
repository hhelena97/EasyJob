package de.hbrs.easyjob.security;

import de.hbrs.easyjob.controllers.LoginController;
import de.hbrs.easyjob.entities.Person;
import de.hbrs.easyjob.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Collections;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends VaadinWebSecurity {

    @Autowired
    private PersonRepository personRepository;
    private LoginController loginController;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("resources/META-INF.resources/**").permitAll() // Ignoriere diese Pfade für Authentifizierung
                // Weitere Konfigurationen hier, falls erforderlich
                .and()
                .csrf().disable(); // Deaktiviere CSRF-Schutz


        super.configure(http);

        setLoginView(http, de.hbrs.easyjob.views.allgemein.LoginView.class);
    }
/*
    @Bean
    public UserDetailsService userDetailsService(){

        UserDetails userDetails = User.withDefaultPasswordEncoder()
                .username(loginController.getPerson().getEmail())
                .password(loginController.getPerson().getPasswort())
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(userDetails);

    }


 */
}
