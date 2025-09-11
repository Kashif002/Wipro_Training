package com.myfinbank.admin.service;

import com.myfinbank.admin.dto.CustomerDTO;
import com.myfinbank.admin.repository.CustomerDataRepository;
import com.myfinbank.admin.repository.LoanApplicationRepository;
import com.myfinbank.admin.exception.CustomerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerManagementService {

    @Autowired
    private CustomerDataRepository customerDataRepository;

    @Autowired
    private LoanApplicationRepository loanApplicationRepository;
    
    @Autowired
    private EmailNotificationService emailNotificationService;

    // Get all customers for admin management
    public List<CustomerDTO> getAllCustomers() {
        List<CustomerDTO> customers = customerDataRepository.findAll();
        // Add loan application counts
        for (CustomerDTO customer : customers) {
            customer.setActiveLoanApplications(
                loanApplicationRepository.findByCustomerId(customer.getId()).size()
            );
        }
        return customers;
    }

    // Get active customers only
    public List<CustomerDTO> getActiveCustomers() {
        List<CustomerDTO> customers = customerDataRepository.findActiveCustomers();
        // Add loan application counts
        for (CustomerDTO customer : customers) {
            customer.setActiveLoanApplications(
                loanApplicationRepository.findByCustomerId(customer.getId()).size()
            );
        }
        return customers;
    }

    // Search customers by name or email
    public List<CustomerDTO> searchCustomers(String searchTerm) {
        List<CustomerDTO> customers = customerDataRepository.searchCustomers(searchTerm);
        // Add loan application counts
        for (CustomerDTO customer : customers) {
            customer.setActiveLoanApplications(
                loanApplicationRepository.findByCustomerId(customer.getId()).size()
            );
        }
        return customers;
    }

    // Get customer details by ID
    public CustomerDTO getCustomerById(Long customerId) {
        CustomerDTO customer = customerDataRepository.findCustomerWithStats(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + customerId));
        
        // Add loan application count if not already set
        if (customer.getActiveLoanApplications() == null) {
            customer.setActiveLoanApplications(
                loanApplicationRepository.findByCustomerId(customerId).size()
            );
        }
        return customer;
    }
    
    // ***** FIX: Added new method to find customer by String customerId *****
    public CustomerDTO getCustomerByCustomerId(String customerId) {
        CustomerDTO customer = customerDataRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with Customer ID: " + customerId));
        
        if (customer.getActiveLoanApplications() == null) {
            customer.setActiveLoanApplications(
                loanApplicationRepository.findByCustomerId(customer.getId()).size()
            );
        }
        return customer;
    }


    // Activate/deactivate customer account
    public CustomerDTO toggleCustomerStatus(Long customerId) {
        CustomerDTO customer = customerDataRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + customerId));

        // Store the previous status to check if we're deactivating
        boolean wasActive = customer.getActive() != null && customer.getActive();
        
        // Toggle active status
        boolean success;
        if (wasActive) {
            success = customerDataRepository.deactivateCustomer(customerId);
            
            // Send deactivation email if customer was successfully deactivated
            if (success) {
                try {
                    // Get account number for the customer (assuming first account)
                    String accountNumber = getCustomerAccountNumber(customerId);
                    emailNotificationService.sendAccountDeactivationEmail(customerId, accountNumber);
                } catch (Exception e) {
                    // Log error but don't fail the deactivation process
                    System.err.println("Failed to send deactivation email: " + e.getMessage());
                }
            }
        } else {
            success = customerDataRepository.activateCustomer(customerId);
        }
        
        if (!success) {
            throw new RuntimeException("Failed to update customer status");
        }
        
        // Return updated customer
        return getCustomerById(customerId);
    }

    // Get recently registered customers
    public List<CustomerDTO> getRecentCustomers() {
        List<CustomerDTO> customers = customerDataRepository.findRecentCustomers(10);
        // Add loan application counts
        for (CustomerDTO customer : customers) {
            customer.setActiveLoanApplications(
                loanApplicationRepository.findByCustomerId(customer.getId()).size()
            );
        }
        return customers;
    }
    
    // Get count of active customers
    public Long getActiveCustomerCount() {
        return customerDataRepository.countActiveCustomers();
    }
    
    // Get count of customers created after date
    public Long getCustomersCreatedAfter(LocalDateTime startDate) {
        return customerDataRepository.countCustomersCreatedAfter(startDate);
    }
    
    // Get customer transactions
    public List<Map<String, Object>> getCustomerTransactions(Long customerId) {
        try {
            System.out.println("CustomerManagementService: Fetching transactions for customer ID: " + customerId);
            
            // First, let's check if the customer has any accounts
            String accountCheckSql = "SELECT COUNT(*) FROM accounts WHERE customer_id = ?";
            int accountCount = customerDataRepository.getJdbcTemplate().queryForObject(accountCheckSql, Integer.class, customerId);
            System.out.println("Customer has " + accountCount + " accounts");
            
            if (accountCount == 0) {
                System.out.println("No accounts found for customer " + customerId);
                return List.of();
            }
            
            // Query to get all transactions for the customer
            String sql = """
                SELECT 
                    t.transaction_id,
                    t.amount,
                    t.type,
                    t.status,
                    t.description,
                    t.reference,
                    t.created_at,
                    fa.account_number as from_account,
                    ta.account_number as to_account
                FROM transactions t
                LEFT JOIN accounts fa ON t.from_account_id = fa.id
                LEFT JOIN accounts ta ON t.to_account_id = ta.id
                WHERE (fa.customer_id = ? OR ta.customer_id = ?)
                ORDER BY t.created_at DESC
                LIMIT 50
                """;
            
            List<Map<String, Object>> transactions = customerDataRepository.getJdbcTemplate().query(sql, (rs, rowNum) -> {
                Map<String, Object> transaction = new HashMap<>();
                transaction.put("transactionId", rs.getString("transaction_id"));
                transaction.put("amount", rs.getBigDecimal("amount"));
                transaction.put("type", rs.getString("type"));
                transaction.put("status", rs.getString("status"));
                transaction.put("description", rs.getString("description"));
                transaction.put("reference", rs.getString("reference"));
                transaction.put("createdAt", rs.getTimestamp("created_at"));
                transaction.put("fromAccount", rs.getString("from_account"));
                transaction.put("toAccount", rs.getString("to_account"));
                return transaction;
            }, customerId, customerId);
            
            System.out.println("Found " + transactions.size() + " transactions for customer " + customerId);
            return transactions;
            
        } catch (Exception e) {
            System.err.println("Error fetching customer transactions: " + e.getMessage());
            e.printStackTrace(); // Add stack trace for debugging
            return List.of();
        }
    }

    // Helper method to get customer's account number
    private String getCustomerAccountNumber(Long customerId) {
        try {
            // Query to get the first account number for the customer
            String sql = "SELECT account_number FROM accounts WHERE customer_id = ? LIMIT 1";
            return customerDataRepository.getJdbcTemplate().queryForObject(sql, String.class, customerId);
        } catch (Exception e) {
            // Return a default account number if not found
            return "ACC" + customerId;
        }
    }
}