package com.example.HardMoneyLending.dto.chat.stats;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class ChatStatsRequest {
    private String senderId;
    private String senderUserType;
    private String recipientId;
    private String recipientUserType;
    private String lastMessageContent;
}
