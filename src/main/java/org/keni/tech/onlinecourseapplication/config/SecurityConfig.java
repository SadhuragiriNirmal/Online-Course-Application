package org.keni.tech.onlinecourseapplication.config;

import org.keni.tech.onlinecourseapplication.util.SecurityUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService UserDetailsService;
    private final JwtFilterChain jwtFilterChain;

    public SecurityConfig(org.springframework.security.core.userdetails.UserDetailsService userDetailsService, JwtFilterChain jwtFilterChain) {
        this.UserDetailsService = userDetailsService;
        this.jwtFilterChain = jwtFilterChain;
    }

    @Bean
    public AuthenticationProvider authProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(UserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request ->
                        request.requestMatchers(HttpMethod.GET, "/course/author/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/course/author/***").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/course/author/***").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/course/author/***").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/course/customer/***").hasRole("CUSTOMER")
                                .requestMatchers(HttpMethod.PUT, "/cart/load/course").hasRole("CUSTOMER")
                                .requestMatchers(HttpMethod.DELETE, "/cart/**").hasRole("CUSTOMER")
                                .requestMatchers(HttpMethod.POST, "/payment/**").hasRole("CUSTOMER")
                                .requestMatchers(HttpMethod.PUT, "/user/customer/**").hasRole("CUSTOMER")
                                .requestMatchers(HttpMethod.DELETE, "/user/customer/***").hasRole("CUSTOMER")
                                .requestMatchers(HttpMethod.PUT, "/user/admin/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/user/admin/**").hasRole("ADMIN")
                                .anyRequest().permitAll()
                )
                .addFilterBefore(jwtFilterChain, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public SecurityUtil getSecurityUtil() {
        return new SecurityUtil();
    }

    @Bean
    public AuthenticationManager getAuthenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}


