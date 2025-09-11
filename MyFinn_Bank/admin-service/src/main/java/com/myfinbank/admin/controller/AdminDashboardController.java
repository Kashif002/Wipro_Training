package com.myfinbank.admin.controller;

import com.myfinbank.admin.dto.DashboardStatsDTO;
import com.myfinbank.admin.dto.CustomerDTO;
import com.myfinbank.admin.dto.LoanApprovalDTO;
import com.myfinbank.admin.dto.ChatMessageDTO;
import com.myfinbank.admin.service.AdminChatService;
import com.myfinbank.admin.service.CustomerManagementService;
import com.myfinbank.admin.service.LoanApprovalService;
import com.myfinbank.admin.repository.CustomerDataRepository;
import com.myfinbank.admin.repository.LoanApplicationRepository;
import com.myfinbank.admin.repository.ChatMessageRepository;
import com.myfinbank.admin.entity.LoanApplication;
import com.myfinbank.admin.entity.ChatMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    private final CustomerDataRepository customerDataRepository;
    private final LoanApplicationRepository loanApplicationRepository;
    private final AdminChatService chatService;
    private final CustomerManagementService customerService;
    private final LoanApprovalService loanService;

    // Constructor injection
    public AdminDashboardController(CustomerDataRepository customerDataRepository,
                                  LoanApplicationRepository loanApplicationRepository,
                                  AdminChatService chatService,
                                  CustomerManagementService customerService,
                                  LoanApprovalService loanService) {
        this.customerDataRepository = customerDataRepository;
        this.loanApplicationRepository = loanApplicationRepository;
        this.chatService = chatService;
        this.customerService = customerService;
        this.loanService = loanService;
    }

    // FIXED: Admin dashboard page - now properly fetches data
    @GetMapping("/dashboard")
    public String dashboardPage(Model model) {
        try {
            System.out.println("=== Dashboard Data Loading Started ===");
            
            // Fetch all required statistics
            System.out.println("Fetching customers...");
            long totalCustomers = customerDataRepository.findAll().size();
            System.out.println("Total customers found: " + totalCustomers);
            
            long activeCustomers = customerDataRepository.countActiveCustomers();
            System.out.println("Active customers found: " + activeCustomers);
            
            // Count loans by status
            System.out.println("Fetching loans...");
            long pendingLoans = loanApplicationRepository.countByStatus(LoanApplication.LoanStatus.PENDING);
            System.out.println("Pending loans found: " + pendingLoans);
            
            long approvedLoans = loanApplicationRepository.countByStatus(LoanApplication.LoanStatus.APPROVED);
            System.out.println("Approved loans found: " + approvedLoans);
            
            // Calculate total loan amount
            BigDecimal totalLoanAmount = loanApplicationRepository.sumLoanAmountByStatus(LoanApplication.LoanStatus.APPROVED);
            if (totalLoanAmount == null) {
                totalLoanAmount = BigDecimal.ZERO;
            }
            
            // Count unread messages
            System.out.println("Fetching chat messages...");
            long unreadMessages = chatService.getUnreadMessageCount();
            System.out.println("Unread messages found: " + unreadMessages);
            
            // Get recent data (limit to 5 items)
            List<CustomerDTO> recentCustomers = customerService.getRecentCustomers()
                    .stream().limit(5).collect(Collectors.toList());
            List<LoanApprovalDTO> recentLoans = loanService.getLoanApplicationsByStatus(LoanApplication.LoanStatus.PENDING)
                    .stream().limit(5).collect(Collectors.toList());
            List<ChatMessageDTO> recentMessages = chatService.getRecentMessages(5);
            
            // Add all data to model for Thymeleaf template
            model.addAttribute("totalCustomers", totalCustomers);
            model.addAttribute("activeCustomers", activeCustomers);
            model.addAttribute("pendingLoans", pendingLoans);
            model.addAttribute("approvedLoans", approvedLoans);
            model.addAttribute("totalLoanAmount", totalLoanAmount);
            model.addAttribute("unreadMessages", unreadMessages);
            model.addAttribute("recentCustomers", recentCustomers);
            model.addAttribute("recentLoans", recentLoans);
            model.addAttribute("recentMessages", recentMessages);
            
            // Debug logging
            System.out.println("Dashboard data loaded successfully:");
            System.out.println("Total Customers: " + totalCustomers);
            System.out.println("Active Customers: " + activeCustomers);
            System.out.println("Pending Loans: " + pendingLoans);
            System.out.println("Approved Loans: " + approvedLoans);
            System.out.println("Unread Messages: " + unreadMessages);
            System.out.println("Recent Customers: " + recentCustomers.size());
            System.out.println("Recent Loans: " + recentLoans.size());
            
        } catch (Exception e) {
            System.err.println("Error loading dashboard data: " + e.getMessage());
            e.printStackTrace();
            
            // Set default values if error occurs
            model.addAttribute("totalCustomers", 0L);
            model.addAttribute("activeCustomers", 0L);
            model.addAttribute("pendingLoans", 0L);
            model.addAttribute("approvedLoans", 0L);
            model.addAttribute("totalLoanAmount", BigDecimal.ZERO);
            model.addAttribute("unreadMessages", 0L);
            model.addAttribute("recentCustomers", List.of());
            model.addAttribute("recentLoans", List.of());
            model.addAttribute("recentMessages", List.of());
            model.addAttribute("error", "Failed to load dashboard data");
        }
        
        model.addAttribute("pageTitle", "Admin Dashboard - MyFin Bank");
        return "admin/dashboard"; // Returns dashboard.html template
    }

    // API endpoint to get dashboard statistics - FOR AJAX CALLS
    @GetMapping("/api/dashboard/stats")
    @ResponseBody
    public DashboardStatsDTO getDashboardStats() {
        DashboardStatsDTO stats = new DashboardStatsDTO();

        try {
            // Get customer statistics
            stats.setTotalCustomers((long) customerDataRepository.findAll().size());
            stats.setActiveCustomers(customerDataRepository.countActiveCustomers());

            // Get loan statistics
            stats.setPendingLoanApplications(
                loanApplicationRepository.countByStatus(LoanApplication.LoanStatus.PENDING)
            );
            stats.setApprovedLoans(
                loanApplicationRepository.countByStatus(LoanApplication.LoanStatus.APPROVED)
            );
            stats.setTotalLoanAmount(
                loanApplicationRepository.sumLoanAmountByStatus(LoanApplication.LoanStatus.APPROVED)
            );

            // Get chat statistics
            stats.setUnreadMessages(chatService.getUnreadMessageCount());

            // Get today's statistics
            LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
            stats.setNewCustomersToday(
                customerDataRepository.countCustomersCreatedAfter(startOfDay)
            );
            stats.setNewLoansToday(
                loanApplicationRepository.countLoansCreatedAfter(startOfDay)
            );
        } catch (Exception e) {
            System.err.println("Error fetching dashboard stats: " + e.getMessage());
        }

        return stats;
    }

    // Get recent activities for dashboard
    @GetMapping("/api/dashboard/recent-activities")
    @ResponseBody
    public Map<String, Object> getRecentActivities() {
        Map<String, Object> activities = new HashMap<>();

        try {
            // Recent customers
            activities.put("recentCustomers", customerService.getRecentCustomers());

            // Recent loan applications
            activities.put("recentLoans", loanService.getLoanApplicationsByStatus(LoanApplication.LoanStatus.PENDING)
                    .stream().limit(5).collect(Collectors.toList()));

            // Recent chat messages
            activities.put("recentMessages", chatService.getRecentMessages(5));
        } catch (Exception e) {
            System.err.println("Error fetching recent activities: " + e.getMessage());
            activities.put("error", e.getMessage());
        }

        return activities;
    }
}
