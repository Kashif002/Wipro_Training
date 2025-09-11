package com.myfinbank.email.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private TemplateService templateService;

    @InjectMocks
    private EmailService emailService;

    @Test
    void testMaskAccountNumber_WithNullAccountNumber() {
        String result = emailService.maskAccountNumber(null);
        assertEquals("N/A", result);
    }

    @Test
    void testMaskAccountNumber_WithShortAccountNumber() {
        String result = emailService.maskAccountNumber("123");
        assertEquals("****123", result);
    }

    @Test
    void testMaskAccountNumber_WithExactlyFourDigits() {
        String result = emailService.maskAccountNumber("1234");
        assertEquals("****1234", result);
    }

    @Test
    void testMaskAccountNumber_WithLongAccountNumber() {
        String result = emailService.maskAccountNumber("1234567890");
        assertEquals("****7890", result);
    }

    @Test
    void testMaskAccountNumber_WithVeryLongAccountNumber() {
        String result = emailService.maskAccountNumber("123456789012345");
        assertEquals("****2345", result);
    }

    @Test
    void testMaskAccountNumber_WithEmptyString() {
        String result = emailService.maskAccountNumber("");
        assertEquals("****", result);
    }
}
