package com.onlineexammodule.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.onlineexammodule.backend.service.ExaminerDetailService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final ExaminerDetailService userDetailsService;

    public SecurityConfig(ExaminerDetailService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private ExamineeJwtFilter examineeJwtFilter;

    @Bean
    @Order(1)
    public SecurityFilterChain examinerSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
            .securityMatcher("/api/examiner/**", "/api/exam/**", "/api/mcqQuestion/**", "/api/programmingQuestion/**")  // Apply only to examiner paths
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/examiner/signin", "/api/examiner/login").permitAll()
                .anyRequest().authenticated())
            .httpBasic(Customizer.withDefaults())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    @Bean
    @Order(2)
     public SecurityFilterChain examineeSecurityFilterChain(HttpSecurity http) throws Exception {
    return http
        .securityMatcher("/api/examinee/**")  // Apply only to examinee paths
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/examinee/login").permitAll()
            .anyRequest().authenticated())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(examineeJwtFilter, UsernamePasswordAuthenticationFilter.class)  // Examinee filter
        .build();
}

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12); // Password strength parameter
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    
}
