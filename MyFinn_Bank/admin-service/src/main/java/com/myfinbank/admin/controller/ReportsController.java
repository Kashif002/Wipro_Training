package com.myfinbank.admin.controller;

import com.myfinbank.admin.service.CustomerManagementService;
import com.myfinbank.admin.service.LoanApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin/reports")
public class ReportsController {

    @Autowired
    private CustomerManagementService customerService;

    @Autowired
    private LoanApprovalService loanService;

    // Reports page
    @GetMapping("")
    public String reportsPage(Model model) {
        model.addAttribute("pageTitle", "Reports & Analytics - MyFin Bank");
        return "admin/reports"; // Returns reports.html template
    }

    // API: Get comprehensive dashboard statistics
    @GetMapping("/api/dashboard-stats")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        try {
            Map<String, Object> stats = new HashMap<>();
            
            // Customer statistics
            stats.put("totalCustomers", customerService.getAllCustomers().size());
            stats.put("activeCustomers", customerService.getActiveCustomers().size());
            
            // Loan statistics
            stats.put("pendingLoans", loanService.getPendingLoanApplications().size());
            
            // Get approved loans count using the available method
            int approvedCount = loanService.getLoanApplicationsByStatus(
                com.myfinbank.admin.entity.LoanApplication.LoanStatus.APPROVED).size();
            stats.put("approvedLoans", approvedCount);
            
            // Calculate total loans (pending + approved + rejected)
            int rejectedCount = loanService.getLoanApplicationsByStatus(
                com.myfinbank.admin.entity.LoanApplication.LoanStatus.REJECTED).size();
            stats.put("totalLoans", loanService.getPendingLoanApplications().size() + approvedCount + rejectedCount);
            
            return ResponseEntity.ok(stats);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to fetch dashboard statistics");
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // API: Get monthly report data
    @GetMapping("/api/monthly")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getMonthlyReports() {
        try {
            Map<String, Object> monthlyData = new HashMap<>();
            
            // For now, return placeholder data
            // In a real implementation, you'd query the database for monthly statistics
            monthlyData.put("message", "Monthly reports functionality coming soon");
            monthlyData.put("currentMonth", java.time.LocalDate.now().getMonth().toString());
            
            return ResponseEntity.ok(monthlyData);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to fetch monthly reports");
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // API: Export report data
    @GetMapping("/api/export")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> exportReports(@RequestParam(required = false) String type) {
        try {
            Map<String, Object> exportData = new HashMap<>();
            
            exportData.put("message", "Export functionality coming soon");
            exportData.put("exportType", type != null ? type : "all");
            exportData.put("timestamp", java.time.LocalDateTime.now().toString());
            
            return ResponseEntity.ok(exportData);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to export reports");
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
