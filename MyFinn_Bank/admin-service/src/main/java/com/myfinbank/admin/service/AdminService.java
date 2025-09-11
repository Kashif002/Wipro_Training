package com.myfinbank.admin.service;

import com.myfinbank.admin.entity.Admin;
import com.myfinbank.admin.dto.AdminDTO;
import com.myfinbank.admin.repository.AdminRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class AdminService implements UserDetailsService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found: " + email));

        return User.builder()
                .username(admin.getEmail())
                .password(admin.getPassword())
                .authorities("ROLE_ADMIN") // Simple - just ADMIN role
                .accountExpired(false)
                .accountLocked(!admin.getIsActive())
                .credentialsExpired(false)
                .disabled(!admin.getIsActive())
                .build();
    }

    public AdminDTO registerAdmin(Admin admin) {
        // Check if email exists
        if (adminRepository.existsByEmail(admin.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Encrypt password and set role
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.setRole("ADMIN"); // Always set to ADMIN
        admin.setCreatedDate(LocalDateTime.now());

        Admin savedAdmin = adminRepository.save(admin);
        return convertToDTO(savedAdmin);
    }

    public Admin findByEmail(String email) {
        return adminRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Admin not found: " + email));
    }

    public void updateLastLogin(String email) {
        Admin admin = findByEmail(email);
        admin.setLastLoginDate(LocalDateTime.now());
        adminRepository.save(admin);
    }

    public boolean existsByEmail(String email) {
        return adminRepository.existsByEmail(email);
    }

    // MISSING METHOD - ADD THIS
    public AdminDTO updateAdmin(Long adminId, Admin adminUpdate) {
        // Find existing admin
        Admin existingAdmin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found with id: " + adminId));
        
        // Update admin fields (only update non-null values)
        if (adminUpdate.getFirstName() != null && !adminUpdate.getFirstName().trim().isEmpty()) {
            existingAdmin.setFirstName(adminUpdate.getFirstName());
        }
        if (adminUpdate.getLastName() != null && !adminUpdate.getLastName().trim().isEmpty()) {
            existingAdmin.setLastName(adminUpdate.getLastName());
        }
        if (adminUpdate.getPhoneNumber() != null && !adminUpdate.getPhoneNumber().trim().isEmpty()) {
            existingAdmin.setPhoneNumber(adminUpdate.getPhoneNumber());
        }
        
        // Don't allow email or role updates for security reasons
        // Don't allow password updates through this method
        
        // Save updated admin
        Admin savedAdmin = adminRepository.save(existingAdmin);
        
        // Convert to DTO and return
        return convertToDTO(savedAdmin);
    }

    private AdminDTO convertToDTO(Admin admin) {
        AdminDTO dto = new AdminDTO();
        dto.setAdminId(admin.getAdminId());
        dto.setEmail(admin.getEmail());
        dto.setFirstName(admin.getFirstName());
        dto.setLastName(admin.getLastName());
        dto.setPhoneNumber(admin.getPhoneNumber());
        dto.setRole(admin.getRole());
        dto.setIsActive(admin.getIsActive());
        return dto;
    }
}
