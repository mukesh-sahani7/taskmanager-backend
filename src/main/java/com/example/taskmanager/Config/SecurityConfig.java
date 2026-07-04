package com.example.taskmanager.Config;
package com.example.taskmanager.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // CORS ko inline hi config kar rahe hain taaki koi alag bean load hone ka jhanjhat hi na rahe
            .cors(cors -> cors.configurationSource(request -> {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(List.of("*"));
                config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                config.setAllowedHeaders(List.of("*")); // Saare headers allow karo
                return config;
            }))
            .csrf(csrf -> csrf.disable()) // CSRF disable
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() //  LIVE ENVIRONMENT MEIN SAARE ENDPOINTS APNE AAP PERMIT HO JAYENGE
            );

        return http.build();
    }
}
