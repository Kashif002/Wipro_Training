package com.myfinbank.email.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequest {
    
    @NotBlank(message = "Recipient email is required")
    @Email(message = "Invalid email format")
    private String to;
    
    @NotBlank(message = "Subject is required")
    private String subject;
    
    @NotBlank(message = "Template name is required")
    private String templateName;
    
    @NotNull(message = "Variables map is required")
    private Map<String, Object> variables;
    
    private String from; // Optional, will use default if not provided
    
    private String priority; // HIGH, MEDIUM, LOW
}
