package com.myfinbank.email.service;

import com.myfinbank.email.dto.EmailRequest;
import com.myfinbank.email.exception.EmailSendingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    
    private final JavaMailSender mailSender;
    private final TemplateService templateService;
    
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    @Async("emailTaskExecutor")
    public CompletableFuture<Void> sendHtmlEmail(EmailRequest request) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            String htmlContent = templateService.processTemplate(request.getTemplateName(), request.getVariables());
            
            helper.setFrom(request.getFrom() != null ? request.getFrom() : fromEmail);
            helper.setTo(request.getTo());
            helper.setSubject(request.getSubject());
            helper.setText(htmlContent, true);
            
            mailSender.send(message);
            log.info("Email sent successfully to: {}", request.getTo());
            
            return CompletableFuture.completedFuture(null);
            
        } catch (MessagingException e) {
            log.error("Failed to send email to: {}", request.getTo(), e);
            throw new EmailSendingException("Failed to send email: " + e.getMessage(), e);
        }
    }
    
    public void sendHtmlEmailSync(EmailRequest request) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            String htmlContent = templateService.processTemplate(request.getTemplateName(), request.getVariables());
            
            helper.setFrom(request.getFrom() != null ? request.getFrom() : fromEmail);
            helper.setTo(request.getTo());
            helper.setSubject(request.getSubject());
            helper.setText(htmlContent, true);
            
            mailSender.send(message);
            log.info("Email sent successfully to: {}", request.getTo());
            
        } catch (MessagingException e) {
            log.error("Failed to send email to: {}", request.getTo(), e);
            throw new EmailSendingException("Failed to send email: " + e.getMessage(), e);
        }
    }
    
    public String maskAccountNumber(String accountNumber) {
        if (accountNumber == null) {
            return "N/A";
        }
        if (accountNumber.length() <= 4) {
            // If account number is 4 digits or less, show it with stars prefix
            return "****" + accountNumber;
        }
        // For longer account numbers, show last 4 digits
        return "****" + accountNumber.substring(accountNumber.length() - 4);
    }
    
    // Getter methods for diagnostic purposes
    public JavaMailSender getMailSender() {
        return mailSender;
    }
    
    public TemplateService getTemplateService() {
        return templateService;
    }
}
