package com.myfinbank.admin.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                        AuthenticationException authException) throws IOException, ServletException {
        String requestUri = request.getRequestURI();
        
        // Check if this is an API request or a page request
        if (requestUri.startsWith("/api/")) {
            // For API requests, return JSON error
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Unauthorized access. Please login as admin.\"}");
        } else {
            // For web page requests, redirect to login
            response.sendRedirect("/login?sessionExpired=true");
        }
    }
}
