package RenergyCartService.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final TokenValidationFilter tokenValidationFilter;

    public SecurityConfig(TokenValidationFilter tokenValidationFilter) {
        this.tokenValidationFilter = tokenValidationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF if using JWTs
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/public/**").permitAll() // Public endpoints
                        .anyRequest().authenticated() // Require authentication for all other endpoints
                )
                .addFilterBefore(tokenValidationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}