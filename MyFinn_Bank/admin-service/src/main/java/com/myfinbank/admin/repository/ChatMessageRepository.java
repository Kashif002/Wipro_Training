package com.myfinbank.admin.repository;

import com.myfinbank.admin.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    
    // Find all messages for a specific customer
    List<ChatMessage> findByCustomerIdOrderByCreatedAtAsc(Long customerId);
    
    // Compatibility method
    default List<ChatMessage> findByCustomerIdOrderByTimestampAsc(Long customerId) {
        return findByCustomerIdOrderByCreatedAtAsc(customerId);
    }
    
    // Find unread messages for admin dashboard
    List<ChatMessage> findByIsReadFalseAndSenderType(ChatMessage.SenderType senderType);
    
    // Count unread messages from customers
    Long countByIsReadFalseAndSenderType(ChatMessage.SenderType senderType);
    
    // Find recent messages for admin dashboard
    List<ChatMessage> findTop10ByOrderByCreatedAtDesc();
    
    // Compatibility method
    default List<ChatMessage> findTop10ByOrderByTimestampDesc() {
        return findTop10ByOrderByCreatedAtDesc();
    }
    
    // Find customers with unread messages (for admin notification)
    @Query("SELECT DISTINCT c.customerId FROM ChatMessage c WHERE c.isRead = false AND c.senderType = :senderType")
    List<Long> findCustomersWithUnreadMessages(@Param("senderType") ChatMessage.SenderType senderType);
    
    // Mark all messages as read for a customer conversation
    @Query("UPDATE ChatMessage c SET c.isRead = true WHERE c.customerId = :customerId")
    void markMessagesAsRead(@Param("customerId") Long customerId);
}
