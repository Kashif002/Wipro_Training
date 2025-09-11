package com.myfinbank.admin.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDTO {
    private Long id;
    private String messageId;
    private Long customerId;
    private String customerName;
    private String message;
    private String senderType;
    private String timestamp;
    private Boolean isRead;
}
