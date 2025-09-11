package com.myfinbank.admin.controller;

import com.myfinbank.admin.dto.ChatMessageDTO;
import com.myfinbank.admin.service.AdminChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/chat")
public class AdminChatController {

    private final AdminChatService chatService;

    // Constructor injection
    public AdminChatController(AdminChatService chatService) {
        this.chatService = chatService;
    }

    // FIXED: Admin chat page - now loads data
    @GetMapping("")
    public String chatPage(Model model) {
        try {
            // Get customers with unread messages for sidebar
            List<Long> customersWithUnread = chatService.getCustomersWithUnreadMessages();
            model.addAttribute("customersWithUnread", customersWithUnread);
            
            // Get recent messages
            List<ChatMessageDTO> recentMessages = chatService.getRecentMessages(10);
            model.addAttribute("recentMessages", recentMessages);
            
            System.out.println("Loaded " + customersWithUnread.size() + " customers with unread messages");
            System.out.println("Loaded " + recentMessages.size() + " recent messages");
        } catch (Exception e) {
            System.err.println("Error loading chat data: " + e.getMessage());
            model.addAttribute("customersWithUnread", List.of());
            model.addAttribute("recentMessages", List.of());
            model.addAttribute("error", "Failed to load chat data");
        }
        
        model.addAttribute("pageTitle", "Customer Chat - MyFin Bank Admin");
        return "admin/chat"; // Returns chat.html template
    }

    // API: Get conversation between admin and specific customer
    @GetMapping("/api/conversation/{customerId}")
    @ResponseBody
    public ResponseEntity<List<ChatMessageDTO>> getConversation(@PathVariable Long customerId) {
        try {
            List<ChatMessageDTO> messages = chatService.getCustomerConversation(customerId);
            // Mark conversation as read when admin opens it
            chatService.markConversationAsRead(customerId);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // API: Send message from admin to customer
    @PostMapping("/api/send")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> sendMessage(@RequestBody Map<String, String> request) {
        try {
            Long customerId = Long.valueOf(request.get("customerId").toString());
            String message = request.get("message").toString();

            // Get current authenticated admin ID
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            // You'll need to get admin ID from email - simplified for now
            Long adminId = 1L; // TODO: Get actual admin ID from authenticated user

            ChatMessageDTO sentMessage = chatService.sendMessageToCustomer(customerId, message, adminId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", sentMessage);
            response.put("status", "Message sent successfully");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Failed to send message: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // API: Get customers with unread messages for admin notification
    @GetMapping("/api/unread-customers")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getUnreadCustomers() {
        try {
            List<Long> customersWithUnread = chatService.getCustomersWithUnreadMessages();
            Long unreadCount = chatService.getUnreadMessageCount();

            Map<String, Object> response = new HashMap<>();
            response.put("customers", customersWithUnread);
            response.put("totalUnread", unreadCount);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to get unread messages");
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // API: Mark conversation as read
    @PostMapping("/api/mark-read/{customerId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> markConversationRead(@PathVariable Long customerId) {
        try {
            chatService.markConversationAsRead(customerId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Conversation marked as read");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Failed to mark conversation as read");
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // API: Get conversation summary for admin dashboard
    @GetMapping("/api/summary")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getConversationSummary() {
        try {
            Map<Long, ChatMessageDTO> summary = chatService.getConversationSummary();
            return ResponseEntity.ok(Map.of("summary", summary));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // API: Get recent messages for chat sidebar
    @GetMapping("/api/recent-messages")
    @ResponseBody
    public ResponseEntity<List<ChatMessageDTO>> getRecentMessages() {
        try {
            List<ChatMessageDTO> recentMessages = chatService.getRecentMessages(20);
            return ResponseEntity.ok(recentMessages);
        } catch (Exception e) {
            System.err.println("Error getting recent messages: " + e.getMessage());
            return ResponseEntity.ok(List.of()); // Return empty list on error
        }
    }
}
