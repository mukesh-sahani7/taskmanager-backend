package com.example.taskmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Password ko secure hash me badalne ke liye
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Stateless API ke liye CSRF disable
                .cors(cors -> cors.configure(http)) // Frontend integration ke liye CORS enable
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()   // Signup aur Login allowed hain
                        .requestMatchers("/api/tasks/**").permitAll()  //Isse saare Tasks operations allow ho jayenge
                        .requestMatchers("/api/notes/**").permitAll() // notes allowed
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}