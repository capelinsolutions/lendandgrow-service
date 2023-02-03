package com.example.HardMoneyLending.dto.chat.stats;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatHistoryResponse {

    private String senderId;
    private String name;
    private String picture;
    private String content;
    private String recipientId;
    private String lastMessageAt;
}
