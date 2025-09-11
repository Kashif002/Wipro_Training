package com.myfinbank.admin.repository;

import com.myfinbank.admin.dto.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class CustomerDataRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    private final RowMapper<CustomerDTO> customerRowMapper = new RowMapper<CustomerDTO>() {
        @Override
        public CustomerDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            CustomerDTO dto = new CustomerDTO();
            dto.setId(rs.getLong("id"));
            dto.setCustomerId(rs.getString("customer_id"));
            dto.setEmail(rs.getString("email"));
            dto.setFirstName(rs.getString("first_name"));
            dto.setLastName(rs.getString("last_name"));
            dto.setPhone(rs.getString("phone"));
            dto.setAddress(rs.getString("address"));
            dto.setActive(rs.getBoolean("active"));
            dto.setEmailVerified(rs.getBoolean("email_verified"));
            dto.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
            dto.setUpdatedAt(rs.getObject("updated_at", LocalDateTime.class));
            return dto;
        }
    };
    
    public List<CustomerDTO> findAll() {
        String sql = "SELECT * FROM customers ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, customerRowMapper);
    }
    
    public Optional<CustomerDTO> findById(Long id) {
        String sql = "SELECT * FROM customers WHERE id = ?";
        List<CustomerDTO> results = jdbcTemplate.query(sql, customerRowMapper, id);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
    
    public Optional<CustomerDTO> findByEmail(String email) {
        String sql = "SELECT * FROM customers WHERE email = ?";
        List<CustomerDTO> results = jdbcTemplate.query(sql, customerRowMapper, email);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
    
    public List<CustomerDTO> findActiveCustomers() {
        String sql = "SELECT * FROM customers WHERE active = true ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, customerRowMapper);
    }
    
    public List<CustomerDTO> searchCustomers(String search) {
        String sql = "SELECT * FROM customers WHERE " +
                     "LOWER(first_name) LIKE LOWER(?) OR " +
                     "LOWER(last_name) LIKE LOWER(?) OR " +
                     "LOWER(email) LIKE LOWER(?)";
        String searchPattern = "%" + search + "%";
        return jdbcTemplate.query(sql, customerRowMapper, searchPattern, searchPattern, searchPattern);
    }
    
    public Long countActiveCustomers() {
        String sql = "SELECT COUNT(*) FROM customers WHERE active = true";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }
    
    public Long countCustomersCreatedAfter(LocalDateTime startDate) {
        String sql = "SELECT COUNT(*) FROM customers WHERE created_at >= ?";
        return jdbcTemplate.queryForObject(sql, Long.class, startDate);
    }
    
    public List<CustomerDTO> findRecentCustomers(int limit) {
        String sql = "SELECT * FROM customers ORDER BY created_at DESC LIMIT ?";
        return jdbcTemplate.query(sql, customerRowMapper, limit);
    }
    
    public boolean deactivateCustomer(Long customerId) {
        String sql = "UPDATE customers SET active = false, updated_at = NOW() WHERE id = ?";
        int updated = jdbcTemplate.update(sql, customerId);
        return updated > 0;
    }
    
    public boolean activateCustomer(Long customerId) {
        String sql = "UPDATE customers SET active = true, updated_at = NOW() WHERE id = ?";
        int updated = jdbcTemplate.update(sql, customerId);
        return updated > 0;
    }
    
    // Find customer by customer_id string field
    public Optional<CustomerDTO> findByCustomerId(String customerId) {
        String sql = "SELECT * FROM customers WHERE customer_id = ?";
        List<CustomerDTO> results = jdbcTemplate.query(sql, customerRowMapper, customerId);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
    
    // Get customer with account statistics
    public Optional<CustomerDTO> findCustomerWithStats(Long customerId) {
        String sql = """
            SELECT 
                c.*,
                COUNT(DISTINCT a.id) as total_accounts,
                COUNT(DISTINCT CASE WHEN l.status = 'APPLIED' THEN l.id END) as pending_loans,
                COUNT(DISTINCT CASE WHEN l.status IN ('APPROVED', 'DISBURSED') THEN l.id END) as active_loans
            FROM customers c
            LEFT JOIN accounts a ON c.id = a.customer_id
            LEFT JOIN loans l ON c.id = l.customer_id
            WHERE c.id = ?
            GROUP BY c.id
        """;
        
        List<CustomerDTO> results = jdbcTemplate.query(sql, (rs, rowNum) -> {
            CustomerDTO dto = customerRowMapper.mapRow(rs, rowNum);
            dto.setTotalAccounts(rs.getInt("total_accounts"));
            dto.setPendingLoans(rs.getInt("pending_loans"));
            dto.setActiveLoans(rs.getInt("active_loans"));
            return dto;
        }, customerId);
        
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
    
    // Getter for JdbcTemplate to be used by other services
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
}
