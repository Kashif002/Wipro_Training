package com.myfinbank.admin.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "admins")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @Column(name = "first_name", nullable = false)
    private String firstName;
    
    @Column(name = "last_name", nullable = false)
    private String lastName;
    
    @Column(name = "phone_number", nullable = false)
    private String phone;
    
    // Simplified - just one role: ADMIN
    @Column(nullable = false)
    private String role = "ADMIN";
    
    @Column(name = "is_active", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean active = true;
    
    @Column(name = "created_date", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "last_login_date")
    private LocalDateTime lastLoginAt;
    
    // Getter/setter compatibility methods for old field names
    public Long getAdminId() { return id; }
    public void setAdminId(Long id) { this.id = id; }
    
    public String getPhoneNumber() { return phone; }
    public void setPhoneNumber(String phone) { this.phone = phone; }
    
    public Boolean getIsActive() { return active; }
    public void setIsActive(Boolean active) { this.active = active; }
    
    public LocalDateTime getCreatedDate() { return createdAt; }
    public void setCreatedDate(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getLastLoginDate() { return lastLoginAt; }
    public void setLastLoginDate(LocalDateTime lastLoginAt) { this.lastLoginAt = lastLoginAt; }
}
