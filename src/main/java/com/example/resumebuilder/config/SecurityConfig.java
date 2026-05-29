package com.example.resumebuilder.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.resumebuilder.security.JwtAuthenticationFilter;
import com.example.resumebuilder.security.JwtAuthenticationImplement;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final JwtAuthenticationImplement jwtAuthenticationImplement;

    //////////////////////////////////////////////////////
    // PASSWORD ENCODER
    //////////////////////////////////////////////////////

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    //////////////////////////////////////////////////////
    // SECURITY FILTER CHAIN
    //////////////////////////////////////////////////////

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http
    ) throws Exception {

        http

            //////////////////////////////////////////////////////
            // CORS
            //////////////////////////////////////////////////////

            .cors(cors ->
                    cors.configurationSource(
                            corsConfigurationSource()
                    )
            )

            //////////////////////////////////////////////////////
            // CSRF DISABLE
            //////////////////////////////////////////////////////

            .csrf(csrf -> csrf.disable())

            //////////////////////////////////////////////////////
            // STATELESS SESSION
            //////////////////////////////////////////////////////

            .sessionManagement(session ->
                    session.sessionCreationPolicy(
                            SessionCreationPolicy.STATELESS
                    )
            )

            //////////////////////////////////////////////////////
            // AUTHORIZATION
            //////////////////////////////////////////////////////

            .authorizeHttpRequests(auth -> auth

                    //////////////////////////////////////////////////////
                    // PUBLIC ROUTES
                    //////////////////////////////////////////////////////

                    .requestMatchers(

                            "/api/auth/**",

                            "/api/payment/verify"

                    ).permitAll()

                    //////////////////////////////////////////////////////
                    // PROTECTED ROUTES
                    //////////////////////////////////////////////////////

                    .anyRequest().authenticated()
            )

            //////////////////////////////////////////////////////
            // EXCEPTION HANDLING
            //////////////////////////////////////////////////////

            .exceptionHandling(ex ->
                    ex.authenticationEntryPoint(
                            jwtAuthenticationImplement
                    )
            )

            //////////////////////////////////////////////////////
            // JWT FILTER
            //////////////////////////////////////////////////////

            .addFilterBefore(
                    jwtAuthenticationFilter,
                    UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }

    //////////////////////////////////////////////////////
    // CORS CONFIGURATION
    //////////////////////////////////////////////////////

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        configuration.setAllowedOrigins(
                Arrays.asList(
                        "http://localhost:5173"
                )
        );

        configuration.setAllowedMethods(
                Arrays.asList(
                        "GET",
                        "POST",
                        "PUT",
                        "PATCH",
                        "DELETE",
                        "OPTIONS"
                )
        );

        configuration.setAllowedHeaders(
                Arrays.asList("*")
        );

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;
    }
}