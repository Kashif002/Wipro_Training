package com.myfinbank.admin.service;

import com.myfinbank.admin.entity.LoanApplication;
import com.myfinbank.admin.dto.CustomerDTO;
import com.myfinbank.admin.repository.CustomerDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
@Slf4j
public class EmailNotificationService {

    @Autowired
    private CustomerDataRepository customerDataRepository;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${email-service.name:email-service}")
    private String emailServiceName;
    
    private final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    // Send loan approval email
    public void sendLoanApprovalEmail(Long customerId, LoanApplication loan) {
        try {
            CustomerDTO customer = customerDataRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found: " + customerId));
            
            String url = "http://localhost:8083/api/emails/loan-approval";
            
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("to", customer.getEmail());
            params.add("customerName", customer.getFirstName() + " " + customer.getLastName());
            params.add("loanType", formatLoanType(loan.getLoanType().toString()));
            params.add("requestedAmount", formatCurrency(loan.getRequestedAmount()));
            params.add("applicationId", loan.getLoanId() != null ? loan.getLoanId().toString() : "APP" + loan.getId());
            params.add("applicationDate", loan.getAppliedAt().format(dateFormatter));
            
            log.info("Sending loan approval email to: {} via service: {}", customer.getEmail(), emailServiceName);
            
            ResponseEntity<Map> response = restTemplate.postForEntity(url, params, Map.class);
            
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Loan approval email sent successfully to: {}", customer.getEmail());
            } else {
                log.warn("Failed to send loan approval email. Status: {}", response.getStatusCode());
            }
            
        } catch (Exception e) {
            log.error("Error sending loan approval email to customer: {} - {}", customerId, e.getMessage());
        }
    }

    // Send loan rejection email
    public void sendLoanRejectionEmail(Long customerId, LoanApplication loan) {
        try {
            CustomerDTO customer = customerDataRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found: " + customerId));
            
            String url = "http://localhost:8083/api/emails/loan-rejected";
            
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("to", customer.getEmail());
            params.add("customerName", customer.getFirstName() + " " + customer.getLastName());
            params.add("loanType", formatLoanType(loan.getLoanType().toString()));
            params.add("requestedAmount", formatCurrency(loan.getRequestedAmount()));
            params.add("applicationId", loan.getLoanId() != null ? loan.getLoanId().toString() : "APP" + loan.getId());
            params.add("applicationDate", loan.getAppliedAt().format(dateFormatter));
            
            log.info("Sending loan rejection email to: {} via service: {}", customer.getEmail(), emailServiceName);
            
            ResponseEntity<Map> response = restTemplate.postForEntity(url, params, Map.class);
            
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Loan rejection email sent successfully to: {}", customer.getEmail());
            } else {
                log.warn("Failed to send loan rejection email. Status: {}", response.getStatusCode());
            }
            
        } catch (Exception e) {
            log.error("Error sending loan rejection email to customer: {} - {}", customerId, e.getMessage());
        }
    }

    // Send account deactivation email
    public void sendAccountDeactivationEmail(Long customerId, String accountNumber) {
        try {
            CustomerDTO customer = customerDataRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found: " + customerId));
            
            String url = "http://" + emailServiceName + "/api/emails/account-deactivated";
            
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("to", customer.getEmail());
            params.add("customerName", customer.getFirstName() + " " + customer.getLastName());
            params.add("accountNumber", accountNumber);
            
            log.info("Sending account deactivation email to: {} via service: {}", customer.getEmail(), emailServiceName);
            
            ResponseEntity<Map> response = restTemplate.postForEntity(url, params, Map.class);
            
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Account deactivation email sent successfully to: {}", customer.getEmail());
            } else {
                log.warn("Failed to send account deactivation email. Status: {}", response.getStatusCode());
            }
            
        } catch (Exception e) {
            log.error("Error sending account deactivation email to customer: {} - {}", customerId, e.getMessage());
        }
    }
    
    // Send zero balance alert to admin
    public void sendZeroBalanceAlertToAdmin(String adminEmail, CustomerDTO customer, String accountNumber) {
        try {
            String url = "http://" + emailServiceName + "/api/emails/balance-alert";
            
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("to", adminEmail);
            params.add("customerName", customer.getFirstName() + " " + customer.getLastName() + " (" + customer.getEmail() + ")");
            params.add("accountNumber", accountNumber);
            
            log.info("Sending zero balance alert to admin: {} via service: {}", adminEmail, emailServiceName);
            
            ResponseEntity<Map> response = restTemplate.postForEntity(url, params, Map.class);
            
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Zero balance alert sent successfully to admin: {}", adminEmail);
            } else {
                log.warn("Failed to send zero balance alert. Status: {}", response.getStatusCode());
            }
            
        } catch (Exception e) {
            log.error("Error sending zero balance alert to admin: {} - {}", adminEmail, e.getMessage());
        }
    }
    
    // Helper methods
    private String formatCurrency(BigDecimal amount) {
        if (amount == null) return "₹0.00";
        return currencyFormatter.format(amount.doubleValue());
    }
    
    private String formatLoanType(String loanType) {
        if (loanType == null) return "";
        // Convert LOAN_TYPE to Loan Type
        String[] words = loanType.replace("_", " ").toLowerCase().split(" ");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (word.length() > 0) {
                result.append(Character.toUpperCase(word.charAt(0)))
                      .append(word.substring(1))
                      .append(" ");
            }
        }
        return result.toString().trim();
    }
}
