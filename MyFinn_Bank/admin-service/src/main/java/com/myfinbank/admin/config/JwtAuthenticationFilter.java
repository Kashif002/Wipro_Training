package com.myfinbank.admin.config;

import com.myfinbank.admin.service.AdminService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final AdminService adminService;

    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider, AdminService adminService) {
        this.tokenProvider = tokenProvider;
        this.adminService = adminService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            String jwt = getJwtFromRequest(request);
            String requestURI = request.getRequestURI();

            if (requestURI.startsWith("/admin/")) {
                System.out.println("[JWT Filter] Processing request: " + requestURI);
                System.out.println("[JWT Filter] Token found: " + (jwt != null ? "Yes" : "No"));
            }

            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                String email = tokenProvider.getEmailFromToken(jwt);
                System.out.println("[JWT Filter] Valid token for user: " + email);

                UserDetails userDetails = adminService.loadUserByUsername(email);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("[JWT Filter] Authentication set successfully");
            } else if (StringUtils.hasText(jwt)) {
                System.out.println("[JWT Filter] Invalid or expired token");
            }

        } catch (Exception ex) {
            System.err.println("[JWT Filter] Error: " + ex.getMessage());
            logger.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        // First, check Authorization header (for AJAX calls)
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        // Fallback: check HTTP-only cookies (for web navigation)
        if (request.getCookies() != null) {
            for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
                if ("adminToken".equals(cookie.getName()) || "jwt".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    if (StringUtils.hasText(token)) {
                        return token;
                    }
                }
            }
        }
        
        return null;
    }
}
