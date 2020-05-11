package com.example.auth.config;

import com.example.auth.service.CustomDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter  {

    private final CustomDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    final
    SimpleCORSFilter simpleCORSFilter;

    public WebSecurityConfig(CustomDetailsService userDetailsService, PasswordEncoder passwordEncoder, SimpleCORSFilter simpleCORSFilter) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.simpleCORSFilter = simpleCORSFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http    .cors().and()
                //.csrf().and()
                .addFilterBefore(simpleCORSFilter, BasicAuthenticationFilter.class)
                //.requireCsrfProtectionMatcher(new AntPathRequestMatcher("/oauth/token")).disable()
                //.anonymous().disable()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                //.antMatchers("/users/current").hasRole("USER")
                //.antMatchers("/users", "/users/*").permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/oauth/token").permitAll()
                .antMatchers("/login", "/oauth/authorize", "/actuator", "/testHealth").permitAll()
                .anyRequest().hasAnyRole("USER", "ADMIN")
                //.and().oauth2Login()
                .and().formLogin().permitAll()
                .and().logout().permitAll();
                //.and().exceptionHandling()
                //.accessDeniedHandler(new OAuth2AccessDeniedHandler());
        // @formatter:on
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
