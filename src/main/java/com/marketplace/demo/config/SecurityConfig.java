package com.marketplace.demo.config;

import com.marketplace.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, proxyTargetClass = true)
public class SecurityConfig extends GlobalAuthenticationConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userService);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .exceptionHandling(configurer ->
                        configurer.accessDeniedPage("/403")
                )
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/**", "/about", "/login", "/register", "/viewphoto/**", "/info").permitAll()
                                .requestMatchers("/details/**", "/saveItem/**", "/profile", "/addItem", "/uploadimg", "/items/**").authenticated()
                )
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/login").permitAll()
                                .usernameParameter("user_email")
                                .passwordParameter("user_password")
                                .loginProcessingUrl("/auth").permitAll()
                                .failureUrl("/login-error")
                                .defaultSuccessUrl("/profile")
                )
                .logout(logout ->
                        logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/login").permitAll()
                );

        /*
        http.
                csrf(csrf ->
                        csrf
                                .disable()
                );
        */

        return http.build();
    }
}
