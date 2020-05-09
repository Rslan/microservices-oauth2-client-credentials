package com.example.oauth.resourceserver.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Configures our application with Spring Security to restrict access to our API endpoints.
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        /*
        This is where we configure the security required for our endpoints and setup our app to serve as
        an OAuth2 Resource Server, using JWT validation.
        */
        http.authorizeRequests()
            .mvcMatchers("/api/public").permitAll()
            .mvcMatchers("/api/private").authenticated()
            .mvcMatchers("/api/private-scoped").hasAuthority("SCOPE_read:messages")
            .and()
            .oauth2ResourceServer()
            .jwt();
    }
}
