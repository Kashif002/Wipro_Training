package com.myfinbank.admin.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
    private Long id; // Primary key from customers table
    private String customerId; // Unique customer identifier
    private String email;
    private String firstName;
    private String lastName;
    private String phone; // Changed from phoneNumber to phone
    private String address;
    private Boolean active; // Changed from isActive to active
    private Boolean emailVerified;
    private LocalDateTime createdAt; // Changed from createdDate to createdAt
    private LocalDateTime updatedAt;
    
    // Additional computed fields for admin dashboard
    private Integer totalAccounts;
    private Integer activeLoans;
    private Integer pendingLoans;
    private Integer activeLoanApplications; // For compatibility
    
    // Compatibility methods for old field names
    public String getPhoneNumber() { return phone; }
    public void setPhoneNumber(String phone) { this.phone = phone; }
    
    public Boolean getIsActive() { return active; }
    public void setIsActive(Boolean active) { this.active = active; }
    
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    
    public String getCreatedDate() { 
        return createdAt != null ? createdAt.toLocalDate().toString() : null; 
    }
    public void setCreatedDate(String date) { 
        // This setter is for compatibility, actual date is in createdAt
    }
}
