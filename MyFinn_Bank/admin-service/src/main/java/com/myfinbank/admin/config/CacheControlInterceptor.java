package com.myfinbank.admin.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class CacheControlInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();

        // Apply strong cache control to sensitive admin pages
        if (path.startsWith("/admin/dashboard") || path.startsWith("/admin/customers") ||
            path.startsWith("/admin/loans") || path.startsWith("/admin/chat") ||
            path.startsWith("/api/admin")) {
            
            // Prevent browser from caching these specific pages
            response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Expires", "0");
        }

        return true;
    }
}
