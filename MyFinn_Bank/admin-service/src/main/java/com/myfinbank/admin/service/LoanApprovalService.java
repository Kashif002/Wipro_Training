package com.myfinbank.admin.service;

import com.myfinbank.admin.entity.LoanApplication;
import com.myfinbank.admin.dto.LoanApprovalDTO;
import com.myfinbank.admin.repository.LoanApplicationRepository;
import com.myfinbank.admin.repository.CustomerDataRepository;
import com.myfinbank.admin.exception.LoanProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanApprovalService {

    @Autowired
    private LoanApplicationRepository loanRepository;

    @Autowired
    private CustomerDataRepository customerDataRepository;

    @Autowired
    private EmailNotificationService emailService; // For sending approval/rejection emails

    // Get all pending loan applications for admin review
    public List<LoanApprovalDTO> getPendingLoanApplications() {
        return loanRepository.findByStatusOrderByAppliedAtAsc(LoanApplication.LoanStatus.PENDING)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get loan applications by status
    public List<LoanApprovalDTO> getLoanApplicationsByStatus(LoanApplication.LoanStatus status) {
        return loanRepository.findByStatus(status)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Approve loan application
    public LoanApprovalDTO approveLoan(Long loanId, Long adminId, String remarks) {
        LoanApplication loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanProcessingException("Loan application not found: " + loanId));

        if (loan.getStatus() != LoanApplication.LoanStatus.PENDING) {
            throw new LoanProcessingException("Loan application is not in pending status");
        }

        // Update loan status
        loan.setStatus(LoanApplication.LoanStatus.APPROVED);
        loan.setApprovedBy(adminId);
        loan.setRemarks(remarks);
        loan.setApprovedDate(LocalDateTime.now());

        LoanApplication savedLoan = loanRepository.save(loan);

        // Send approval email to customer
        try {
            emailService.sendLoanApprovalEmail(loan.getCustomerId(), savedLoan);
        } catch (Exception e) {
            // Log email error but don't fail the approval process
            System.err.println("Failed to send approval email: " + e.getMessage());
        }

        return convertToDTO(savedLoan);
    }

    // Reject loan application
    public LoanApprovalDTO rejectLoan(Long loanId, Long adminId, String remarks) {
        LoanApplication loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanProcessingException("Loan application not found: " + loanId));

        if (loan.getStatus() != LoanApplication.LoanStatus.PENDING) {
            throw new LoanProcessingException("Loan application is not in pending status");
        }

        // Update loan status
        loan.setStatus(LoanApplication.LoanStatus.REJECTED);
        loan.setApprovedBy(adminId);
        loan.setRemarks(remarks);
        loan.setApprovedDate(LocalDateTime.now());

        LoanApplication savedLoan = loanRepository.save(loan);

        // Send rejection email to customer
        try {
            emailService.sendLoanRejectionEmail(loan.getCustomerId(), savedLoan);
        } catch (Exception e) {
            // Log email error but don't fail the rejection process
            System.err.println("Failed to send rejection email: " + e.getMessage());
        }

        return convertToDTO(savedLoan);
    }

    // Get loan details by ID
    public LoanApprovalDTO getLoanById(Long loanId) {
        LoanApplication loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanProcessingException("Loan application not found: " + loanId));
        return convertToDTO(loan);
    }

    // Convert LoanApplication entity to DTO
    private LoanApprovalDTO convertToDTO(LoanApplication loan) {
        LoanApprovalDTO dto = new LoanApprovalDTO();
        dto.setLoanId(loan.getLoanId());
        dto.setCustomerId(loan.getCustomerId());
        dto.setLoanAmount(loan.getLoanAmount());
        dto.setLoanType(loan.getLoanType().toString());
        dto.setInterestRate(loan.getInterestRate() != null ? loan.getInterestRate().doubleValue() : null);
        dto.setTermMonths(loan.getTermMonths());
        dto.setStatus(loan.getStatus().toString());
        dto.setPurpose(loan.getPurpose());
        dto.setMonthlyIncome(loan.getMonthlyIncome());
        dto.setEmploymentDetails(loan.getEmploymentDetails());
        dto.setRemarks(loan.getRemarks());
        if (loan.getAppliedAt() != null) {
            dto.setCreatedDate(loan.getAppliedAt()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }

        // Get customer details
        customerDataRepository.findById(loan.getCustomerId()).ifPresent(customer -> {
            dto.setCustomerName(customer.getFirstName() + " " + customer.getLastName());
            dto.setCustomerEmail(customer.getEmail());
        });

        return dto;
    }
}
