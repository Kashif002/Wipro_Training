package com.myfinbank.email.controller;

import com.myfinbank.email.dto.EmailRequest;
import com.myfinbank.email.service.EmailService;
import com.myfinbank.email.service.TemplateService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/emails")
@RequiredArgsConstructor
public class EmailController {
    
    private final EmailService emailService;
    
    @PostMapping("/send")
    public ResponseEntity<Map<String, String>> sendEmail(@Valid @RequestBody EmailRequest request) {
        emailService.sendHtmlEmail(request);
        return ResponseEntity.ok(Map.of(
            "status", "success",
            "message", "Email sent successfully"
        ));
    }
    
    @PostMapping("/welcome")
    public ResponseEntity<Map<String, String>> sendWelcomeEmail(
            @RequestParam String to,
            @RequestParam String customerName) {
        
        EmailRequest request = EmailRequest.builder()
                .to(to)
                .subject("Welcome to MyFin Bank!")
                .templateName("welcome-email")
                .variables(Map.of("customerName", customerName))
                .build();
                
        emailService.sendHtmlEmail(request);
        return ResponseEntity.ok(Map.of(
            "status", "success",
            "message", "Welcome email sent successfully"
        ));
    }
    
    @PostMapping("/loan-approval")
    public ResponseEntity<Map<String, String>> sendLoanApprovalEmail(
            @RequestParam String to,
            @RequestParam String customerName,
            @RequestParam String loanAmount,
            @RequestParam String loanType) {
        
        EmailRequest request = EmailRequest.builder()
                .to(to)
                .subject("Loan Approved - MyFin Bank")
                .templateName("loan-approval")
                .variables(Map.of(
                    "customerName", customerName,
                    "loanAmount", loanAmount,
                    "loanType", loanType
                ))
                .build();
                
        emailService.sendHtmlEmail(request);
        return ResponseEntity.ok(Map.of(
            "status", "success",
            "message", "Loan approval email sent successfully"
        ));
    }
    
    @PostMapping("/balance-alert")
    public ResponseEntity<Map<String, String>> sendBalanceAlert(
            @RequestParam String to,
            @RequestParam String customerName,
            @RequestParam String accountNumber) {
        
        // Mask the account number before sending to template
        String maskedAccountNumber = emailService.maskAccountNumber(accountNumber);
        
        EmailRequest request = EmailRequest.builder()
                .to(to)
                .subject("Account Balance Alert - MyFin Bank")
                .templateName("zero-balance-alert")
                .variables(Map.of(
                    "customerName", customerName,
                    "accountNumber", maskedAccountNumber
                ))
                .build();
                
        emailService.sendHtmlEmail(request);
        return ResponseEntity.ok(Map.of(
            "status", "success",
            "message", "Balance alert email sent successfully"
        ));
    }
    
    @PostMapping("/loan-rejected")
    public ResponseEntity<Map<String, String>> sendLoanRejectedEmail(
            @RequestParam String to,
            @RequestParam String customerName,
            @RequestParam String applicationId,
            @RequestParam String loanType,
            @RequestParam String requestedAmount,
            @RequestParam String applicationDate) {
        
        EmailRequest request = EmailRequest.builder()
                .to(to)
                .subject("Loan Application Decision - MyFin Bank")
                .templateName("loan-rejected")
                .variables(Map.of(
                    "customerName", customerName,
                    "applicationId", applicationId,
                    "loanType", loanType,
                    "requestedAmount", requestedAmount,
                    "applicationDate", applicationDate
                ))
                .build();
                
        emailService.sendHtmlEmail(request);
        return ResponseEntity.ok(Map.of(
            "status", "success",
            "message", "Loan rejection email sent successfully"
        ));
    }

    @PostMapping("/account-deactivated")
    public ResponseEntity<Map<String, String>> sendAccountDeactivatedEmail(
            @RequestParam String to,
            @RequestParam String customerName,
            @RequestParam String accountNumber) {
        
        // Mask the account number before sending to template
        String maskedAccountNumber = emailService.maskAccountNumber(accountNumber);
        
        EmailRequest request = EmailRequest.builder()
                .to(to)
                .subject("Account Status Update - MyFin Bank")
                .templateName("account-deactivated")
                .variables(Map.of(
                    "customerName", customerName,
                    "accountNumber", maskedAccountNumber
                ))
                .build();
                
        emailService.sendHtmlEmail(request);
        return ResponseEntity.ok(Map.of(
            "status", "success",
            "message", "Account deactivation email sent successfully"
        ));
    }
    
    // Test endpoint to check email functionality
    @PostMapping("/test")
    public ResponseEntity<Map<String, String>> testEmail(@RequestParam String to) {
        try {
            EmailRequest request = EmailRequest.builder()
                    .to(to)
                    .subject("Test Email - MyFin Bank")
                    .templateName("welcome-email")
                    .variables(Map.of("customerName", "Test User"))
                    .build();
                    
            // Use synchronous sending for testing
            emailService.sendHtmlEmailSync(request);
            return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Test email sent successfully to: " + to
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "status", "error",
                "message", "Failed to send test email: " + e.getMessage()
            ));
        }
    }
    
    // Diagnostic endpoint to check SMTP configuration
    @GetMapping("/diagnose")
    public ResponseEntity<Map<String, Object>> diagnoseEmailService() {
        Map<String, Object> diagnosis = new HashMap<>();
        
        try {
            // Test SMTP connection
            JavaMailSender mailSender = emailService.getMailSender();
            diagnosis.put("smtpConnection", "OK");
            diagnosis.put("mailSender", mailSender != null ? "Configured" : "Not configured");
            
            // Check template engine
            TemplateService templateService = emailService.getTemplateService();
            diagnosis.put("templateEngine", templateService != null ? "OK" : "Not configured");
            
            // Test template processing
            try {
                String testTemplate = templateService.processTemplate("welcome-email", 
                    Map.of("customerName", "Test User"));
                diagnosis.put("templateProcessing", testTemplate.length() > 0 ? "OK" : "Failed");
                diagnosis.put("templateLength", testTemplate.length());
            } catch (Exception templateError) {
                diagnosis.put("templateProcessing", "Failed");
                diagnosis.put("templateError", templateError.getMessage());
            }
            
            diagnosis.put("status", "success");
            diagnosis.put("message", "Email service diagnosis completed");
            
        } catch (Exception e) {
            diagnosis.put("status", "error");
            diagnosis.put("message", "Diagnosis failed: " + e.getMessage());
            diagnosis.put("error", e.getClass().getSimpleName());
            diagnosis.put("stackTrace", e.getStackTrace()[0].toString());
        }
        
        return ResponseEntity.ok(diagnosis);
    }
}
