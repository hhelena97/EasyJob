package de.hbrs.easyjob.security;

import com.vaadin.flow.spring.security.VaadinWebSecurityConfigurerAdapter;
import de.hbrs.easyjob.services.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

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
                .antMatchers("/login").permitAll()
                .antMatchers("/student/**").hasRole("STUDENT")
                .antMatchers("/unternehmen/**").hasRole("UNTERNEHMENSPERSON")
                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
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
                .passwordEncoder(NoOpPasswordEncoder.getInstance());
    }

}
