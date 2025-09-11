package com.myfinbank.admin.repository;

import com.myfinbank.admin.entity.LoanApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {
    
    // Find loans by status
    List<LoanApplication> findByStatus(LoanApplication.LoanStatus status);
    
    // Find loans by customer ID
    List<LoanApplication> findByCustomerId(Long customerId);
    
    // Find pending loan applications (most important for admin)
    List<LoanApplication> findByStatusOrderByAppliedAtAsc(LoanApplication.LoanStatus status);
    
    // Count loans by status
    Long countByStatus(LoanApplication.LoanStatus status);
    
    // Calculate total approved loan amount
    @Query("SELECT COALESCE(SUM(l.requestedAmount), 0) FROM LoanApplication l WHERE l.status = :status")
    BigDecimal sumLoanAmountByStatus(@Param("status") LoanApplication.LoanStatus status);
    
    // Find loans created today
    @Query("SELECT COUNT(l) FROM LoanApplication l WHERE l.appliedAt >= :startDate")
    Long countLoansCreatedAfter(@Param("startDate") LocalDateTime startDate);
    
    // Find recent loan applications for dashboard
    List<LoanApplication> findTop10ByOrderByAppliedAtDesc();
    
    // Find loans by type
    List<LoanApplication> findByLoanType(LoanApplication.LoanType loanType);
}
