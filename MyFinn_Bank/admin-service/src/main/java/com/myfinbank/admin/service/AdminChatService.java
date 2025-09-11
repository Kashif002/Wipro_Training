package com.myfinbank.admin.service;

import com.myfinbank.admin.entity.ChatMessage;
import com.myfinbank.admin.dto.ChatMessageDTO;
import com.myfinbank.admin.repository.ChatMessageRepository;
import com.myfinbank.admin.repository.CustomerDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AdminChatService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private CustomerDataRepository customerDataRepository;

    // Get all messages for a specific customer conversation
    public List<ChatMessageDTO> getCustomerConversation(Long customerId) {
        return chatMessageRepository.findByCustomerIdOrderByTimestampAsc(customerId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Send message from admin to customer
    public ChatMessageDTO sendMessageToCustomer(Long customerId, String message, Long adminId) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setMessageId("MSG" + System.currentTimeMillis());
        chatMessage.setCustomerId(customerId);
        chatMessage.setMessage(message);
        chatMessage.setSenderType(ChatMessage.SenderType.ADMIN);
        chatMessage.setAdminId(adminId);
        chatMessage.setSenderId(adminId); // Set senderId to adminId for admin messages
        chatMessage.setIsRead(false);
        chatMessage.setCreatedAt(LocalDateTime.now());

        ChatMessage savedMessage = chatMessageRepository.save(chatMessage);
        return convertToDTO(savedMessage);
    }

    // Get customers with unread messages
    public List<Long> getCustomersWithUnreadMessages() {
        return chatMessageRepository.findCustomersWithUnreadMessages(ChatMessage.SenderType.CUSTOMER);
    }

    // Get unread message count for admin dashboard
    public Long getUnreadMessageCount() {
        return chatMessageRepository.countByIsReadFalseAndSenderType(ChatMessage.SenderType.CUSTOMER);
    }

    // Mark conversation as read when admin opens it
    public void markConversationAsRead(Long customerId) {
        // Update all unread customer messages to read
        List<ChatMessage> unreadMessages = chatMessageRepository.findByCustomerIdOrderByTimestampAsc(customerId)
                .stream()
                .filter(msg -> !msg.getIsRead() && msg.getSenderType() == ChatMessage.SenderType.CUSTOMER)
                .collect(Collectors.toList());

        unreadMessages.forEach(msg -> msg.setIsRead(true));
        chatMessageRepository.saveAll(unreadMessages);
    }

    // Get recent messages for admin dashboard
    public List<ChatMessageDTO> getRecentMessages(int limit) {
        return chatMessageRepository.findTop10ByOrderByTimestampDesc()
                .stream()
                .limit(limit)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get conversation summary for admin - shows latest message per customer
    public Map<Long, ChatMessageDTO> getConversationSummary() {
        List<ChatMessage> allMessages = chatMessageRepository.findTop10ByOrderByTimestampDesc();
        
        return allMessages.stream()
                .collect(Collectors.groupingBy(
                    ChatMessage::getCustomerId,
                    Collectors.collectingAndThen(
                        Collectors.maxBy((msg1, msg2) -> msg1.getTimestamp().compareTo(msg2.getTimestamp())),
                        optMsg -> optMsg.map(this::convertToDTO).orElse(null)
                    )
                ))
                .entrySet().stream()
                .filter(entry -> entry.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    // Convert ChatMessage entity to DTO
    private ChatMessageDTO convertToDTO(ChatMessage message) {
        ChatMessageDTO dto = new ChatMessageDTO();
        dto.setId(message.getId());
        dto.setMessageId(message.getMessageId());
        dto.setCustomerId(message.getCustomerId());
        dto.setMessage(message.getMessage());
        dto.setSenderType(message.getSenderType().toString());
        dto.setIsRead(message.getIsRead());
        dto.setTimestamp(message.getTimestamp()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        // Get customer name
        customerDataRepository.findById(message.getCustomerId()).ifPresent(customer -> {
            dto.setCustomerName(customer.getFirstName() + " " + customer.getLastName());
        });

        return dto;
    }
}
