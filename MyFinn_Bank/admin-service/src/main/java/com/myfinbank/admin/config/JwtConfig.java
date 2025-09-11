package com.myfinbank.admin.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtConfig {
    private String secret; // JWT secret key from application.yml
    private long expiration; // Token expiration time in milliseconds
    
    // Default values if not specified in application.yml
    public JwtConfig() {
        this.secret = "myfinbank-admin-default-secret-key";
        this.expiration = 86400000L; // 24 hours
    }
}
