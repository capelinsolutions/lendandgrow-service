package com.example.HardMoneyLending.dto.chat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaginatedChatRequest {
    private String senderId; // User id of a sender
    private String recipientId; // User id of a recipient

    private int pageNo;
    private int pageSize;
}
