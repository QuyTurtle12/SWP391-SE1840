package com.swp391.jewelrysalesystem.config;

import com.swp391.jewelrysalesystem.filters.JwtRequestFilter;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

            @Autowired
            private JwtRequestFilter jwtRequestFilter;

        private static final Logger LOGGER = Logger.getLogger(JwtRequestFilter.class.getName());

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http.csrf(csrf -> csrf.disable())
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/api/auth/login", "/api/auth/forgot-password",
                                                                "/api/auth/reset-password",
                                                                "/api/auth/logout", "/upload/**",
                                                                "/**.jpg", "/**.jpeg", "/**.png")
                                                .permitAll() // Allow access
                                                .requestMatchers("/api/auth/admin/**", "/api/v2/accounts/MANAGER/**",
                                                                "/api/v2/accounts/dashboard")
                                                .hasAuthority("ROLE_ADMIN")
                                                .requestMatchers("/api/v2/accounts/user", "/api/v2/counters/**")
                                                .hasAnyAuthority("ROLE_ADMIN", "ROLE_MANAGER")
                                                .requestMatchers("/api/auth/manager/**",
                                                                "/api/categories/**",
                                                                "/api/v2/accounts/STAFF/**",
                                                                "/api/v2/promotions/**")
                                                .hasAuthority("ROLE_MANAGER")
                                                .requestMatchers("/api/v2/products/{ID}", "/api/v2/products",
                                                                "/api/v2/products/sort",
                                                                "/api/v2/products/search",
                                                                "/api/v2/products/search/sort", "/api/v2/orders/**",
                                                                "/refunds/**",
                                                                "/api/v2/customers/**",
                                                                "/api/v2/customers/promotion/**",
                                                                "/api/customer-promotions/**")
                                                .hasAnyAuthority("ROLE_MANAGER", "ROLE_STAFF")
                                                .requestMatchers("/api/auth/staff/**", "/cart/**",
                                                                "/api/v2/accounts/staff")
                                                .hasAuthority("ROLE_STAFF")

                                                .anyRequest().authenticated()) // Secure other endpoints

                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Add JWT token filter
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
