package de.hbrs.easyjob.security;

import de.hbrs.easyjob.services.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private MyUserDetailService userDetailsService;
    @Autowired
    private CustomSecurityContextRepository customSecurityContextRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .securityContext()
                .securityContextRepository(customSecurityContextRepository)
                .and()
                .authorizeRequests()
                .antMatchers("/login", "/registrieren", "/passwortVergessen").permitAll()
                .antMatchers("/student/**").hasRole("STUDENT")
                .antMatchers("/unternehmen/**").hasRole("UNTERNEHMENSPERSON")
                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .sessionFixation().migrateSession()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(getPasswordEncoder());
    }

    public static Argon2PasswordEncoder getPasswordEncoder() {
        return new Argon2PasswordEncoder(16, 32, 2, 60000, 10);
    }


}
