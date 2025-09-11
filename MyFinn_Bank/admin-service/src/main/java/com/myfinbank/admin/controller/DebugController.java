package com.myfinbank.admin.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/debug")
public class DebugController {

    @GetMapping("/check-auth")
    public Map<String, Object> checkAuthentication(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        
        // Check cookies
        Map<String, String> cookies = new HashMap<>();
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                cookies.put(cookie.getName(), cookie.getValue());
            }
        }
        result.put("cookies", cookies);
        
        // Check authentication
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            result.put("authenticated", auth.isAuthenticated());
            result.put("principal", auth.getPrincipal().toString());
            result.put("authorities", auth.getAuthorities().toString());
        } else {
            result.put("authenticated", false);
            result.put("principal", "No authentication");
        }
        
        // Request info
        result.put("requestURI", request.getRequestURI());
        result.put("sessionId", request.getSession(false) != null ? request.getSession().getId() : "No session");
        
        return result;
    }
}
