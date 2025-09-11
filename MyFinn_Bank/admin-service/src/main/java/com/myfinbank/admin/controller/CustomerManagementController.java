package com.myfinbank.admin.controller;

import com.myfinbank.admin.dto.CustomerDTO;
import com.myfinbank.admin.service.CustomerManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/customers")
@Tag(name = "Customer Management", description = "Customer management and administration APIs")
public class CustomerManagementController {

    private final CustomerManagementService customerService;

    // Constructor injection
    public CustomerManagementController(CustomerManagementService customerService) {
        this.customerService = customerService;
    }

    // FIXED: Customer management page - now loads data
    @GetMapping("")
    public String customersPage(Model model) {
        try {
            List<CustomerDTO> customers = customerService.getAllCustomers();
            model.addAttribute("customers", customers);
            System.out.println("Loaded " + customers.size() + " customers");
        } catch (Exception e) {
            System.err.println("Error loading customers: " + e.getMessage());
            model.addAttribute("customers", List.of());
            model.addAttribute("error", "Failed to load customers");
        }
        model.addAttribute("pageTitle", "Customer Management - MyFin Bank Admin");
        return "admin/customers"; // Returns customers.html template
    }

    // Customer details page - handle both Long ID and String customer ID
    @GetMapping("/{customerId}")
    public String customerDetailsPage(@PathVariable String customerId, Model model) {
        try {
            CustomerDTO customer = null;
            
            // Try to parse as Long ID first (for database primary key)
            try {
                Long id = Long.parseLong(customerId);
                customer = customerService.getCustomerById(id);
            } catch (NumberFormatException e) {
                // If not a number, try as customer ID string
                customer = customerService.getCustomerByCustomerId(customerId);
            }
            
            if (customer == null) {
                model.addAttribute("error", "Customer not found");
                return "admin/customers";
            }
            
            model.addAttribute("customer", customer);
            model.addAttribute("pageTitle", "Customer Details - " + customer.getFirstName() + " " + customer.getLastName());
            return "admin/customer-details";
        } catch (Exception e) {
            System.err.println("Error loading customer details: " + e.getMessage());
            model.addAttribute("error", "Customer not found: " + e.getMessage());
            return "admin/customers";
        }
    }

    // Customer transactions page
    @GetMapping("/{customerId}/transactions")
    public String customerTransactionsPage(@PathVariable String customerId, Model model) {
        try {
            CustomerDTO customer = null;
            
            // Try to parse as Long ID first (for database primary key)
            try {
                Long id = Long.parseLong(customerId);
                customer = customerService.getCustomerById(id);
            } catch (NumberFormatException e) {
                // If not a number, try as customer ID string
                customer = customerService.getCustomerByCustomerId(customerId);
            }
            
            if (customer == null) {
                model.addAttribute("error", "Customer not found");
                return "admin/customers";
            }
            
            model.addAttribute("customerId", customer.getId());
            model.addAttribute("customerName", customer.getFirstName() + " " + customer.getLastName());
            model.addAttribute("pageTitle", "Transaction History - " + customer.getFirstName() + " " + customer.getLastName());
            return "admin/view-transactions";
        } catch (Exception e) {
            System.err.println("Error loading customer transactions page: " + e.getMessage());
            model.addAttribute("error", "Customer not found: " + e.getMessage());
            return "admin/customers";
        }
    }

    // API: Get all customers
    @GetMapping("/api/all")
    @ResponseBody
    @Operation(summary = "Get all customers", description = "Retrieve all customers in the system")
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        try {
            List<CustomerDTO> customers = customerService.getAllCustomers();
            return ResponseEntity.ok(customers);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // API: Search customers by keyword (name or email)
    @GetMapping("/api/search")
    @ResponseBody
    @Operation(summary = "Search customers", description = "Search customers by name or email")
    public ResponseEntity<List<CustomerDTO>> searchCustomers(@RequestParam String keyword) {
        try {
            List<CustomerDTO> customers = customerService.searchCustomers(keyword);
            return ResponseEntity.ok(customers);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // API: Get active customers only
    @GetMapping("/api/active")
    @ResponseBody
    @Operation(summary = "Get active customers", description = "Retrieve only active customers")
    public ResponseEntity<List<CustomerDTO>> getActiveCustomers() {
        try {
            List<CustomerDTO> activeCustomers = customerService.getActiveCustomers();
            return ResponseEntity.ok(activeCustomers);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // API: Get customer details by ID
    @GetMapping("/api/{customerId}")
    @ResponseBody
    @Operation(summary = "Get customer by ID", description = "Retrieve customer details by customer ID")
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable Long customerId) {
        try {
            CustomerDTO customer = customerService.getCustomerById(customerId);
            return ResponseEntity.ok(customer);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // API: Toggle customer active status (activate/deactivate)
    @PostMapping("/api/{customerId}/toggle-status")
    @ResponseBody
    @Operation(summary = "Toggle customer status", description = "Activate or deactivate customer account")
    public ResponseEntity<Map<String, Object>> toggleCustomerStatus(@PathVariable Long customerId) {
        try {
            CustomerDTO updatedCustomer = customerService.toggleCustomerStatus(customerId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("customer", updatedCustomer);
            response.put("message", updatedCustomer.getIsActive() ?
                "Customer account activated successfully" :
                "Customer account deactivated successfully");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Failed to update customer status: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // API: Get recently registered customers
    @GetMapping("/api/recent")
    @ResponseBody
    @Operation(summary = "Get recent customers", description = "Retrieve recently registered customers")
    public ResponseEntity<List<CustomerDTO>> getRecentCustomers() {
        try {
            List<CustomerDTO> recentCustomers = customerService.getRecentCustomers();
            return ResponseEntity.ok(recentCustomers);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // API: Get customer statistics for admin dashboard
    @GetMapping("/api/stats")
    @ResponseBody
    @Operation(summary = "Get customer statistics", description = "Retrieve customer statistics for admin dashboard")
    public ResponseEntity<Map<String, Object>> getCustomerStats() {
        try {
            List<CustomerDTO> allCustomers = customerService.getAllCustomers();
            List<CustomerDTO> activeCustomers = customerService.getActiveCustomers();

            Map<String, Object> stats = new HashMap<>();
            stats.put("totalCustomers", allCustomers.size());
            stats.put("activeCustomers", activeCustomers.size());
            stats.put("inactiveCustomers", allCustomers.size() - activeCustomers.size());

            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // API: Get customer transactions
    @GetMapping("/api/{customerId}/transactions")
    @ResponseBody
    @Operation(summary = "Get customer transactions", description = "Retrieve transaction history for a specific customer")
    public ResponseEntity<Map<String, Object>> getCustomerTransactions(@PathVariable Long customerId) {
        try {
            System.out.println("Fetching transactions for customer ID: " + customerId);
            List<Map<String, Object>> transactions = customerService.getCustomerTransactions(customerId);
            System.out.println("Found " + transactions.size() + " transactions");
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("transactions", transactions);
            response.put("count", transactions.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Error in getCustomerTransactions: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Failed to fetch transactions: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}