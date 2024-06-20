package com.stg.recruit.security.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.stg.recruit.entity.enumuration.ERole;

import lombok.RequiredArgsConstructor;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private static final String[] GLOBAL_EXCLUDE_API_PATTERN = new String[] {
            "/api/auth/**"
    };

    private static final String USER_API_PATTERN = "/api/user/**";

    private static final String[] ALLOWED_USER_API_ROLES = new String[] { ERole.RECRUITER_ADMIN.name(), ERole.RECRUITER.name() };

    private final JwtAuthorizationFilter jwtTokenFilter;

    /**
     * Note: If 'server.servlet.context-path' is set then the same
     * value does not need to be provided in the security filter chain's request
     * matcher pattern.
     * 
     * @param http
     * @return
     * @throws Exception throws generic Exception if any error occurs
     * 
     * 
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> {
                auth.requestMatchers(GLOBAL_EXCLUDE_API_PATTERN).permitAll();
                auth.requestMatchers(USER_API_PATTERN).authenticated();
                auth.requestMatchers("/api/auth/adduser").hasAuthority("RECRUITER_ADMIN");
                auth.requestMatchers("/api/user/stg").hasAuthority("RECRUITER_ADMIN");
                auth.requestMatchers("/api/auth/all").hasAuthority("RECRUITER_ADMIN"); 
                auth.requestMatchers("/api/candidates").hasAnyAuthority(ALLOWED_USER_API_ROLES);

                auth.anyRequest().authenticated();
            })
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))    
            .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }


    @Bean
    CorsFilter corsFilter() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200","http://localhost:4100"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization")); // Add the appropriate headers
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return new CorsFilter(source);
    }
}
