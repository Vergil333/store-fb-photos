package com.machava.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/*
*  Overriding Spring Security because of demo
*  Disabling security only because of H2 database console
*  You do not want this on production. There is no reason to have H2 console on production
* */
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests().antMatchers("/").permitAll().and() // allow all requests to the root
                .authorizeRequests().antMatchers("/console/**").permitAll(); // allow all requests to the H2 database console
        httpSecurity.csrf().disable(); // disable CSRF protection
        httpSecurity.headers().frameOptions().disable(); // disable X-Frame option in Spring Security

    }
}
