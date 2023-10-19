package com.junglesoftware.marketplace.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @EnableWebSecurity
    @Profile(value = {"local"})
    public static class LocalSecurityConfig {
        @Bean
        SecurityFilterChain localSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
            return httpSecurity.authorizeHttpRequests(auth -> auth.requestMatchers("/**").permitAll()).build();
        }
    }

    @EnableWebSecurity
    @Profile(value = {"prd", "int", "uat"})
    public static class GlobalSecurityConfig {
        @Bean
        @Order(1)
        SecurityFilterChain apiSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
            return getGlobalFilterChain(httpSecurity);
        }

        @Bean
        @Order(2)
        SecurityFilterChain allowAllFilterChain(HttpSecurity httpSecurity) throws Exception {
            return getMaintenanceFilterChain(httpSecurity);
        }

        private SecurityFilterChain getMaintenanceFilterChain(HttpSecurity httpSecurity) throws Exception {
            return httpSecurity
                    .authorizeHttpRequests(auth -> {
                        auth.requestMatchers("/api/support/**").authenticated();
                        auth.requestMatchers("/api/error").permitAll();
                        auth.anyRequest().authenticated();
                    })
                    .formLogin(Customizer.withDefaults())
                    .build();
        }

        private SecurityFilterChain getGlobalFilterChain(HttpSecurity httpSecurity) throws Exception {
            return httpSecurity
                    .securityMatcher("/api/**")
                    .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .httpBasic(Customizer.withDefaults())
                    .build();
        }
    }
}
