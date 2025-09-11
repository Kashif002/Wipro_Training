package com.myfinbank.admin.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanApprovalDTO {
    private Long loanId;
    private Long customerId;
    private String customerName;
    private String customerEmail;
    private BigDecimal loanAmount;
    private String loanType;
    private Double interestRate;
    private Integer termMonths;
    private String status;
    private String purpose;
    private BigDecimal monthlyIncome;
    private String employmentDetails;
    private String createdDate;
    private String remarks;
}
