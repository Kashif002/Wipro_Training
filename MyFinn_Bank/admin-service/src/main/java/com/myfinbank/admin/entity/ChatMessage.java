package com.myfinbank.admin.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "message_id", nullable = true)
    private String messageId;
    
    @Column(name = "customer_id", nullable = false)
    private Long customerId; // Reference to customer
    
    @Column(name = "admin_id")
    private Long adminId; // Reference to admin (if sender is admin)
    
    @Column(name = "message", columnDefinition = "TEXT", nullable = false)
    private String message;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "sender_type", nullable = false)
    private SenderType senderType; // CUSTOMER or ADMIN
    
    @Column(name = "sender_id")
    private Long senderId;
    
    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false;
    
    @Column(name = "created_at", nullable = false, insertable = true, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    // Enum for sender type
    public enum SenderType {
        CUSTOMER, ADMIN
    }
    
    // Compatibility methods for old field names
    public Boolean getIsRead() { return isRead; }
    public void setIsRead(Boolean isRead) { this.isRead = isRead; }
    
    public LocalDateTime getTimestamp() { return createdAt; }
    public void setTimestamp(LocalDateTime timestamp) { this.createdAt = timestamp; }
}
