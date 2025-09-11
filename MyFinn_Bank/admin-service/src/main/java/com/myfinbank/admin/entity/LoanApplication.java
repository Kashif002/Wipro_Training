package com.myfinbank.admin.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "loans") // Changed from loan_applications to unified loans table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Changed from loanId to id to match loans table
    
    @Column(unique = true)
    private String loanId; // Unique loan identifier
    
    @Column(nullable = false)
    private Long customerId; // Reference to customer
    
    @Column(name = "requested_amount", nullable = false)
    private BigDecimal requestedAmount; // Changed from loanAmount
    
    @Column(name = "approved_amount")
    private BigDecimal approvedAmount;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "loan_type")
    private LoanType loanType;
    
    @Column(name = "interest_rate")
    private BigDecimal interestRate; // Changed from Double to BigDecimal
    
    @Column(name = "term_months")
    private Integer termMonths;
    
    @Column(name = "monthly_emi")
    private BigDecimal monthlyEmi;
    
    @Enumerated(EnumType.STRING)
    private LoanStatus status = LoanStatus.PENDING;
    
    @Column
    private String purpose;
    
    @Column(name = "monthly_income")
    private BigDecimal monthlyIncome;
    
    @Column(name = "employment_details", length = 500)
    private String employmentDetails;
    
    @Column(name = "approved_by")
    private Long approvedBy; // Admin ID who approved
    
    @Column(name = "admin_remarks", length = 500)
    private String adminRemarks; // Changed from remarks
    
    @Column(name = "applied_at", nullable = false)
    private LocalDateTime appliedAt = LocalDateTime.now();
    
    @Column(name = "processed_at")
    private LocalDateTime processedAt;
    
    @Column(name = "approved_date")
    private LocalDateTime approvedDate;
    
    // Enums updated to match loans table
    public enum LoanType {
        PERSONAL, HOME, VEHICLE, EDUCATION, BUSINESS
    }
    
    public enum LoanStatus {
        PENDING, APPROVED, REJECTED
    }
    
    // Compatibility methods for old field names
    public Long getLoanId() { 
        // Try to return the numeric part if loanId is like "LN0001"
        if (loanId != null && loanId.startsWith("LN")) {
            try {
                return Long.parseLong(loanId.substring(2));
            } catch (NumberFormatException e) {
                // Fall back to id if parsing fails
            }
        }
        return id; 
    }
    
    public void setLoanId(Long id) { 
        this.id = id;
        // Also update the string loanId
        this.loanId = "LN" + String.format("%04d", id);
    }
    
    public BigDecimal getLoanAmount() { return requestedAmount; }
    public void setLoanAmount(BigDecimal amount) { this.requestedAmount = amount; }
    
    public String getRemarks() { return adminRemarks; }
    public void setRemarks(String remarks) { this.adminRemarks = remarks; }
    
    public LocalDateTime getApprovedDate() { return approvedDate; }
    public void setApprovedDate(LocalDateTime date) { this.approvedDate = date; }
    
    public Long getApprovedBy() { return approvedBy; }
    public void setApprovedBy(Long adminId) { this.approvedBy = adminId; }
    
    public LoanStatus getStatus() { return status; }
    public void setStatus(LoanStatus status) { this.status = status; }
}
