package com.myfinbank.admin.repository;

import com.myfinbank.admin.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    
    // Find admin by email for authentication
    Optional<Admin> findByEmail(String email);
    
    // Find active admins only
    List<Admin> findByActiveTrue();
    
    // Check if email exists (for registration validation)
    boolean existsByEmail(String email);
    
    // Find admins by role (simplified - now String instead of enum)
    List<Admin> findByRole(String role);
    
    // Custom query to count active admins
    @Query("SELECT COUNT(a) FROM Admin a WHERE a.active = true")
    Long countActiveAdmins();
}
