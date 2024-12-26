package com.fullmoon.jopportal.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {
    private final DataSource DataSource;
//    private final CustomUserDetailsService customUserDetailsService;

//    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
//        this.customUserDetailsService = customUserDetailsService;
//    }
    public SecurityConfig(DataSource DataSource) {
        this.DataSource = DataSource;
    }
    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        jdbcUserDetailsManager.setUsersByUsernameQuery("select email, password, is_active from users where email=?");
//        jdbcUserDetailsManager.setUsersByEmailQuery("select email, password, is_active from users where email=?");
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("select user_type_id, user_type from users where  email=?");
        return jdbcUserDetailsManager;
    }
                                                 @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/home3","register","/register/new","/home","/uploads/**.png","/uploads/**.jpg","/uploads/**").permitAll() // Allow access to /home
                        .anyRequest().authenticated() // All other requests need authentication
                )
                .formLogin(form -> form
                        .loginPage("/login")// Specify the login page
                        .loginProcessingUrl("/signin") // Specify the URL to validate the credentials
                        .defaultSuccessUrl("/explore", true) // Redirect to this URL on successful login
                        .usernameParameter("email") // Specify that the email parameter should be used
                        .permitAll() // Allow everyone to see the login page
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout") // Redirect to the login page on successful logout
                        .permitAll() // Allow everyone to log out
                )
                .rememberMe(rememberMe -> rememberMe
                        .key("remember-me") // Key to identify the cookie
                        .tokenValiditySeconds(24 * 60 * 60) // Token is valid for 1 days
                )
                .userDetailsService(userDetailsManager(DataSource))
//                .userDetailsService(customUserDetailsService)

        ;

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}