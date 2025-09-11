package com.myfinbank.admin.controller;

import com.myfinbank.admin.repository.CustomerDataRepository;
import com.myfinbank.admin.repository.LoanApplicationRepository;
import com.myfinbank.admin.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/test")
public class DatabaseTestController {

    @Autowired
    private CustomerDataRepository customerDataRepository;

    @Autowired
    private LoanApplicationRepository loanApplicationRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @GetMapping("/database")
    public ResponseEntity<Map<String, Object>> testDatabase() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // Test customer repository
            long customerCount = customerDataRepository.findAll().size();
            result.put("customerCount", customerCount);
            result.put("customers", customerDataRepository.findAll());
            
            // Test loan repository
            long loanCount = loanApplicationRepository.count();
            result.put("loanCount", loanCount);
            result.put("loans", loanApplicationRepository.findAll());
            
            // Test chat repository
            long chatCount = chatMessageRepository.count();
            result.put("chatCount", chatCount);
            result.put("chatMessages", chatMessageRepository.findAll());
            
            result.put("status", "success");
            result.put("message", "Database connection successful");
            
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", "Database error: " + e.getMessage());
            result.put("error", e.getClass().getSimpleName());
            e.printStackTrace();
        }
        
        return ResponseEntity.ok(result);
    }
}
