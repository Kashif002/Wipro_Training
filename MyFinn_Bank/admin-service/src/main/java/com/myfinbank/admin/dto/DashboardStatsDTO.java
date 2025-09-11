package com.myfinbank.admin.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDTO {
    private Long totalCustomers;
    private Long activeCustomers;
    private Long pendingLoanApplications;
    private Long approvedLoans;
    private BigDecimal totalLoanAmount;
    private Long unreadMessages;
    private Long newCustomersToday;
    private Long newLoansToday;
}
