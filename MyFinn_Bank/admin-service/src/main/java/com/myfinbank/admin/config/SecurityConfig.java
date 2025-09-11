package com.myfinbank.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    public SecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http.cors().and().csrf().disable()
            .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and()
            // Use stateless session for JWT
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeHttpRequests()
            // Public endpoints
            .requestMatchers("/", "/index", "/home", "/login", "/register").permitAll()
            .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
            .requestMatchers("/api/admin/login", "/api/admin/register").permitAll()
            .requestMatchers("/api/admin/validate-session").permitAll()
            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", "/webjars/**", "/springdoc/**").permitAll()
            .requestMatchers("/actuator/**").permitAll()
            .requestMatchers("/api/debug/**").permitAll()
            .requestMatchers("/h2-console/**").permitAll()
            // Admin pages require authentication
            .requestMatchers("/admin/**").authenticated()
            .requestMatchers("/api/admin/**").authenticated()
            .anyRequest().authenticated()
            .and()
            .logout()
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login?logout=true")
            .invalidateHttpSession(true)
            .clearAuthentication(true)
            .deleteCookies("JSESSIONID", "jwt", "adminToken")
            .addLogoutHandler((request, response, authentication) -> {
                // Additional cleanup
                response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
                response.setHeader("Pragma", "no-cache");
                response.setHeader("Expires", "0");
            })
            .permitAll();

        // Add JWT filter before UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // For H2 console
        http.headers().frameOptions().sameOrigin();

        return http.build();
    }
}
