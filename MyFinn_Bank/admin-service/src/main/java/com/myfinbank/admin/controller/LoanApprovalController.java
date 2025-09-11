package com.myfinbank.admin.controller;

import com.myfinbank.admin.dto.LoanApprovalDTO;
import com.myfinbank.admin.service.LoanApprovalService;
import com.myfinbank.admin.entity.LoanApplication;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/loans")
@Tag(name = "Loan Management", description = "Loan approval and management APIs")
public class LoanApprovalController {

    private final LoanApprovalService loanService;

    // Constructor injection
    public LoanApprovalController(LoanApprovalService loanService) {
        this.loanService = loanService;
    }

    // FIXED: Loan approval page - now loads data
    @GetMapping("")
    public String loansPage(Model model) {
        try {
            List<LoanApprovalDTO> loans = loanService.getLoanApplicationsByStatus(LoanApplication.LoanStatus.PENDING);
            model.addAttribute("loans", loans);
            System.out.println("Loaded " + loans.size() + " loans");
        } catch (Exception e) {
            System.err.println("Error loading loans: " + e.getMessage());
            model.addAttribute("loans", List.of());
            model.addAttribute("error", "Failed to load loans");
        }
        model.addAttribute("pageTitle", "Loan Management - MyFin Bank Admin");
        return "admin/loans"; // Returns loans.html template
    }

    // Loan details page
    @GetMapping("/{loanId}")
    public String loanDetailsPage(@PathVariable Long loanId, Model model) {
        try {
            LoanApprovalDTO loan = loanService.getLoanById(loanId);
            model.addAttribute("loan", loan);
            model.addAttribute("pageTitle", "Loan Details - Application #" + loanId);
            return "admin/loan-details"; // Returns loan-details.html template
        } catch (Exception e) {
            model.addAttribute("error", "Loan application not found");
            return "redirect:/admin/loans";
        }
    }

    // API: Get all pending loan applications
    @GetMapping("/api/pending")
    @ResponseBody
    @Operation(summary = "Get pending loans", description = "Retrieve all pending loan applications")
    public ResponseEntity<List<LoanApprovalDTO>> getPendingLoans() {
        try {
            List<LoanApprovalDTO> pendingLoans = loanService.getPendingLoanApplications();
            return ResponseEntity.ok(pendingLoans);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // API: Get loans by status
    @GetMapping("/api/status/{status}")
    @ResponseBody
    @Operation(summary = "Get loans by status", description = "Retrieve loans filtered by status")
    public ResponseEntity<List<LoanApprovalDTO>> getLoansByStatus(@PathVariable String status) {
        try {
            LoanApplication.LoanStatus loanStatus = LoanApplication.LoanStatus.valueOf(status.toUpperCase());
            List<LoanApprovalDTO> loans = loanService.getLoanApplicationsByStatus(loanStatus);
            return ResponseEntity.ok(loans);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // API: Approve loan application
    @PostMapping("/api/approve/{loanId}")
    @ResponseBody
    @Operation(summary = "Approve loan", description = "Approve a loan application")
    public ResponseEntity<Map<String, Object>> approveLoan(@PathVariable Long loanId,
                                                          @RequestBody Map<String, String> request) {
        try {
            // Get current authenticated admin ID
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            // TODO: Get actual admin ID from authenticated user - simplified for now
            Long adminId = 1L;

            String remarks = request.getOrDefault("remarks", "Loan approved by admin");

            LoanApprovalDTO approvedLoan = loanService.approveLoan(loanId, adminId, remarks);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("loan", approvedLoan);
            response.put("message", "Loan application approved successfully");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Failed to approve loan: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // API: Reject loan application
    @PostMapping("/api/reject/{loanId}")
    @ResponseBody
    @Operation(summary = "Reject loan", description = "Reject a loan application")
    public ResponseEntity<Map<String, Object>> rejectLoan(@PathVariable Long loanId,
                                                         @RequestBody Map<String, String> request) {
        try {
            // Get current authenticated admin ID
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            // TODO: Get actual admin ID from authenticated user - simplified for now
            Long adminId = 1L;

            String remarks = request.getOrDefault("remarks", "Loan rejected by admin");

            LoanApprovalDTO rejectedLoan = loanService.rejectLoan(loanId, adminId, remarks);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("loan", rejectedLoan);
            response.put("message", "Loan application rejected");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Failed to reject loan: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // API: Get loan details by ID
    @GetMapping("/api/{loanId}")
    @ResponseBody
    @Operation(summary = "Get loan details", description = "Retrieve detailed information for a specific loan")
    public ResponseEntity<LoanApprovalDTO> getLoanDetails(@PathVariable Long loanId) {
        try {
            LoanApprovalDTO loan = loanService.getLoanById(loanId);
            return ResponseEntity.ok(loan);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // API: Get loan statistics for dashboard
    @GetMapping("/api/stats")
    @ResponseBody
    @Operation(summary = "Get loan statistics", description = "Retrieve loan statistics for admin dashboard")
    public ResponseEntity<Map<String, Object>> getLoanStats() {
        try {
            List<LoanApprovalDTO> pendingLoans = loanService.getLoanApplicationsByStatus(LoanApplication.LoanStatus.PENDING);
            List<LoanApprovalDTO> approvedLoans = loanService.getLoanApplicationsByStatus(LoanApplication.LoanStatus.APPROVED);
            List<LoanApprovalDTO> rejectedLoans = loanService.getLoanApplicationsByStatus(LoanApplication.LoanStatus.REJECTED);

            Map<String, Object> stats = new HashMap<>();
            stats.put("pendingLoans", pendingLoans.size());
            stats.put("approvedLoans", approvedLoans.size());
            stats.put("rejectedLoans", rejectedLoans.size());
            stats.put("totalApplications", pendingLoans.size() + approvedLoans.size() + rejectedLoans.size());

            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // API: Process multiple loans (batch approval/rejection)
    @PostMapping("/api/batch-process")
    @ResponseBody
    @Operation(summary = "Batch process loans", description = "Process multiple loans in batch")
    public ResponseEntity<Map<String, Object>> batchProcessLoans(@RequestBody Map<String, Object> request) {
        try {
            // Get current authenticated admin ID
            Long adminId = 1L; // TODO: Get actual admin ID

            @SuppressWarnings("unchecked")
            List<Long> loanIds = (List<Long>) request.get("loanIds");
            String action = request.get("action").toString(); // "approve" or "reject"
            String remarks = request.getOrDefault("remarks", "").toString();

            Map<String, Object> response = new HashMap<>();
            int successCount = 0;
            int errorCount = 0;

            for (Long loanId : loanIds) {
                try {
                    if ("approve".equals(action)) {
                        loanService.approveLoan(loanId, adminId, remarks);
                    } else if ("reject".equals(action)) {
                        loanService.rejectLoan(loanId, adminId, remarks);
                    }
                    successCount++;
                } catch (Exception e) {
                    errorCount++;
                }
            }

            response.put("success", true);
            response.put("message", String.format("Processed %d loans successfully, %d failed", successCount, errorCount));
            response.put("successCount", successCount);
            response.put("errorCount", errorCount);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Batch processing failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
