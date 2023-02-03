package com.example.HardMoneyLending.dto.chat;

import com.example.HardMoneyLending.models.chat.message.ChatMessage;
import com.example.HardMoneyLending.models.property.Property;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PaginatedChatResponse {
    private List<ChatMessage> chatMessages;

    private int pageNo;
    private int pageSize;
    private int noOfPages;
    private long totalRows;
}
