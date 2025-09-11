package com.myfinbank.admin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC configuration to register interceptors and configure web behavior
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private CacheControlInterceptor cacheControlInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Register the cache control interceptor for admin pages
        registry.addInterceptor(cacheControlInterceptor)
                .addPathPatterns("/admin/**", "/api/admin/**")
                .excludePathPatterns("/admin/login", "/admin/register");
    }
}
